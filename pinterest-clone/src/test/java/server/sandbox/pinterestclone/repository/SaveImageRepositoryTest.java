package server.sandbox.pinterestclone.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SaveImageRepositoryTest {

    @Autowired
    private SaveImageRepository saveImageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

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

        SaveImage saveImage = SaveImage.createSaveImage(user, image);
        saveImageRepository.addSaveImage(saveImage);

        Assertions.assertThat(saveImage.getId()).isNotNull();
        Assertions.assertThat(saveImage.getImage()).isEqualTo(image);
        Assertions.assertThat(saveImage.getUser()).isEqualTo(user);
    }

    @Test
    void findByUserIdAndImageID() {
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

        SaveImage saveImage = SaveImage.createSaveImage(user, image);
        saveImageRepository.addSaveImage(saveImage);

        SaveImage findSaveImage2 = saveImageRepository.findByUserIdAndImageID(user, image);
        Assertions.assertThat(findSaveImage2).isEqualTo(saveImage);
    }
}