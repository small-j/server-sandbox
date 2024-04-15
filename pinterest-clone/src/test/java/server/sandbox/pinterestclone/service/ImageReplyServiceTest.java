package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.ImageReplyRequest;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.repository.ImageReplyRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ImageReplyServiceTest {

    @Autowired
    private ImageReplyService imageReplyService;
    @Autowired
    private ImageReplyRepository imageReplyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    void addReply() {
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

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), user.getId(), "");
        int id = imageReplyService.addReply(imageReplyRequest);

        Assertions.assertThat(imageReplyRepository.findById(id)).isNotNull();
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

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), tempId, "");
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> imageReplyService.addReply(imageReplyRequest));
    }

    @Test
    void checkNotExistImage() {
        int tempId = 1;
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();
        userRepository.register(user);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(tempId, user.getId(), "");
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> imageReplyService.addReply(imageReplyRequest));
    }
}