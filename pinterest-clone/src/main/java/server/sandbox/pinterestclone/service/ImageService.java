package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.*;
import server.sandbox.pinterestclone.domain.dto.*;
import server.sandbox.pinterestclone.repository.*;
import server.sandbox.pinterestclone.service.exception.ErrorMessage;
import server.sandbox.pinterestclone.storage.StorageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final ImageCategoryRepository imageCategoryRepository;
    private final SaveImageRepository saveImageRepository;
    private final UserRepository userRepository;
    private final StorageManager storageManager;

    public ImageResponse uploadImage(FileRequest imageRequest) {
        String key = UUID.randomUUID().toString();
        String url = "";
        try {
            url = storageManager.uploadFile(key, imageRequest);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ErrorMessage.FAIL_READ_INPUT_STREAM.getMessage());
        }

        return new ImageResponse(key, url);
    }

    @Transactional
    public Integer addImage(ImageMetaRequest imageMetaRequest) {
        User user = userRepository.findById(imageMetaRequest.getUserId());
        validateUser(user);
        imageMetaRequest.getCategoryIds().stream().forEach((categoryId) -> {
            Category category = categoryRepository.findById(categoryId);
            validateCategory(category);
        });

        // 이미지 정보 추가
        Image image = Image.builder()
                .title(imageMetaRequest.getTitle())
                .content(imageMetaRequest.getContent())
                .key(imageMetaRequest.getKey())
                .url(imageMetaRequest.getUrl())
                .build();
        image.setUser(user);

        imageRepository.addImage(image);

        // 이미지 카테고리 추가
        List<ImageCategory> imageCategories = imageMetaRequest.getCategoryIds()
                .stream()
                .map(categoryId -> ImageCategory.create(image, categoryId))
                .toList();

        imageCategories
                .stream()
                .forEach(imageCategory -> imageCategoryRepository.addImageCategory(imageCategory));

        return image.getId();
    }

    @Transactional
    public Integer deleteImage(int imageId) {
        log.info("delete image");
        Image image = imageRepository.findById(imageId);
        deleteS3Image(image);
        deleteSaveImage(image);
        imageRepository.deleteImage(image);

        return imageId;
    }

    @Transactional
    public ImageDetailInfoResponse findImage(int id, int userId) {
        Image image = imageRepository.findById(id);
        validateImage(image);

        List<ImageReplyResponse> imageReplyResponses = image.getImageReplies()
                .stream()
                .map(imageReply -> ImageReplyResponse.of(imageReply))
                .toList();

        // 현재 이미지 카테고리 리스트 가져오기
        // 카테고리와 일치하는 이미지 정보들 생성 날짜로 내림차순 정렬해서 가져오기
        List<ImageCategory> imageCategories = image.getImageCategories();
        List<Integer> categoryIds = imageCategories.stream().map(imageCategory -> imageCategory.getCategoryId()).toList();

        // TODO : categoryIds가 없거나 추가 이미지를 불러오지 않도록 수정.
        List<Image> moreImages = imageRepository.findImagesWithSimilarCategories(categoryIds, id);

        if (userId != -1) addUserImageHistory(image, userId); // TODO : refactor.

        return ImageDetailInfoResponse.of(image, imageReplyResponses, moreImages);
    }

    public List<ImageMetaSimpleResponse> getMainImages(int userId) {
        User user = userRepository.findById(userId);
        validateUser(user);

        List<Image> images = imageRepository.getImageFromImageHistory(user);
        List<Integer> categoryIds = imageRepository.getImageCategoryIdFromImages(images);
        List<Image> recommendRandomImages = imageRepository.getRecommendRandomImages(categoryIds);

        return ImageMetaSimpleResponse.of(recommendRandomImages);
    }

    public List<ImageMetaSimpleResponse> getSearchImages(String searchStr) {
        validateSearchString(searchStr);

        List<Image> imageTitleOrContentRelationalImages = imageRepository.getImageTitleOrContentRelationalImages(searchStr);
        List<ImageCategory> imageCategories = imageCategoryRepository.getCategoryFromSearchWord(searchStr);
        List<Image> categoryRelationalImages = new ArrayList<>();

        if (imageCategories.size() > 0)
            categoryRelationalImages = imageRepository.getCategoryRelationalImages(imageCategories);

        return ImageMetaSimpleResponse.of(combineList(categoryRelationalImages, imageTitleOrContentRelationalImages));
    }

    @Transactional
    public void addUserImageHistory(Image image, int userId) {
        User user = userRepository.findById(userId);
        validateUser(user);

        UserImageHistory userImageHistory = UserImageHistory.builder()
                .image(image)
                .user(user)
                .build();
        user.addImageHistory(userImageHistory);

        imageRepository.addUserImageHistory(userImageHistory);
    }

    private void validateImage(Image image) {
        if (ObjectUtils.isEmpty(image))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_IMAGE.getMessage());
    }

    private void validateUser(User user) {
        if (ObjectUtils.isEmpty(user))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_USER.getMessage());
    }

    private void validateSearchString(String searchStr) {
        if (searchStr.length() <= 0)
            throw new IllegalArgumentException(ErrorMessage.CAN_NOT_SEARCH_STRING.getMessage());
    }

    private void validateCategory(Category category) {
        if (ObjectUtils.isEmpty(category))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_CATEGORY.getMessage());
    }

    private void deleteS3Image(Image image) {
        storageManager.deleteFile(image.getKey());
    }

    private void deleteSaveImage(Image image) {
        saveImageRepository.deleteSaveImageToImage(image);
    }

    private List<Image> combineList(List<Image> a, List<Image> b) {
        List<Image> result = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();

        for (Image image : a) {
            if (!ids.contains(image.getId())) {
                ids.add(image.getId());
                result.add(image);
            }
        }
        for (Image image : b) {
            if (!ids.contains(image.getId())) {
                ids.add(image.getId());
                result.add(image);
            }
        }

        return result;
    }
}
