package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.ImageCategory;
import server.sandbox.pinterestclone.domain.dto.ImageMetaRequest;
import server.sandbox.pinterestclone.domain.dto.ImageMetaResponse;
import server.sandbox.pinterestclone.repository.ImageCategoryRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    private final ImageRepository imageRepository;
    private final ImageCategoryRepository imageCategoryRepository;

    @Transactional
    public ImageMetaResponse addImage(ImageMetaRequest imageMetaRequest) {
        // 이미지 정보 추가
        Image image = Image.builder()
                .title(imageMetaRequest.getTitle())
                .content(imageMetaRequest.getContent())
                .build();

        // 이미지 업로드
        image.uploadImage(imageMetaRequest.getImageData());
        imageRepository.addImage(image);

        // 이미지 카테고리 추가
        List<ImageCategory> imageCategories = imageMetaRequest.getCategories()
                .stream()
                .map(categoryId -> ImageCategory.create(image, categoryId))
                .toList();

        imageCategories
                .stream()
                .forEach(imageCategory -> imageCategoryRepository.addImageCategory(imageCategory));

        return ImageMetaResponse.of(image);
    }
}
