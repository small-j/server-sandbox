package server.sandbox.pinterestclone.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;

@SpringBootTest
@Transactional
public class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;

    @Test
    void addImage() {
        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        imageRepository.addImage(image);
        Assertions.assertThat(imageRepository.findById(image.getId())).isNotNull();
    }
}
