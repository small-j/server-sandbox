package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.ImageCategory;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.*;
import server.sandbox.pinterestclone.repository.ImageCategoryRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.SaveImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;
import server.sandbox.pinterestclone.storage.StorageManager;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageCategoryRepository imageCategoryRepository;
    private final SaveImageRepository saveImageRepository;
    private final UserRepository userRepository;
    private final StorageManager storageManager;

    private String FAIL_READ_INPUT_STREAM = "Fail to read input stream data";

    public ImageResponse uploadImage(FileRequest imageRequest) {
        String key = UUID.randomUUID().toString();
        String url = "";
        try {
            url = storageManager.uploadFile(key, imageRequest);
        } catch (IOException ex) {
            throw new IllegalArgumentException(FAIL_READ_INPUT_STREAM);
        }

        return new ImageResponse(key, url);
    }

    @Transactional
    public Integer addImage(ImageMetaRequest imageMetaRequest) {
        User user = userRepository.findById(imageMetaRequest.getUserId());

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
        List<ImageCategory> imageCategories = imageMetaRequest.getCategories()
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

    private void deleteS3Image(Image image) {
        storageManager.deleteFile(image.getKey());
    }

    private void deleteSaveImage(Image image) {
        saveImageRepository.deleteSaveImageToImage(image);
    }
}
