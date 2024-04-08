package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.SaveImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;

@SpringBootTest
@Transactional
class SaveImageServiceTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaveImageRepository saveImageRepository;
    @Autowired
    private SaveImageService saveImageService;

    @Test
    void addSaveImage() {
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userRepository.register(user);
        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(user.getId(), image.getId());
        int id = saveImageService.addSaveImage(saveImageRequest);

        Assertions.assertThat(saveImageRepository.findById(id)).isNotNull();
    }

    @Test
    void checkNotExistUser() {
        int tempId = 1;
        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(tempId, image.getId());
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> saveImageService.addSaveImage(saveImageRequest));
    }

    @Test
    void checkNotExistImage() {
        int tempId = 1;
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();
        userRepository.register(user);

        SaveImageRequest saveImageRequest = new SaveImageRequest(user.getId(), tempId);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> saveImageService.addSaveImage(saveImageRequest));
    }

    @Test
    void checkSaveImageDuplication() {
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userRepository.register(user);
        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(user.getId(), image.getId());
        saveImageService.addSaveImage(saveImageRequest);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> saveImageService.addSaveImage(saveImageRequest));
    }
}