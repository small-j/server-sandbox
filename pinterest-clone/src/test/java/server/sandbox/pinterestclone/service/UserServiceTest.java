package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private SaveImageService saveImageService;

    @Test
    void register() {
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .build();

        int id = userService.register(userRequest);

        Assertions.assertThat(userRepository.findById(id)).isNotNull();
    }

    @Test
    void checkEmailDuplication() {
        UserRequest user1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("김지윤")
                .build();

        UserRequest user2 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("김지윤")
                .build();

        userService.register(user1);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> userService.register(user2));
    }

    @Test
    void getUserInfo() {
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();

        List<Image> images = new ArrayList<>();
        images.add(Image.builder()
                .title("test")
                .content("test")
                .url("test1")
                .key("test")
                .build());
        images.add(Image.builder()
                .title("test")
                .content("test")
                .url("test2")
                .key("test")
                .build());

        userRepository.register(user);
        images.stream().forEach(image -> imageRepository.addImage(image));

        images.stream().forEach(image -> saveImageService.addSaveImage(new SaveImageRequest(user.getId(), image.getId())));

        UserInfoResponse userInfoResponse = userService.getUserInfo(user.getId());
        Assertions.assertThat(userInfoResponse.getUserResponse().getEmail()).isEqualTo(user.getEmail());
        Assertions.assertThat(userInfoResponse.getImageUrls().size()).isEqualTo(images.size());

        int index = 0;
        for (Image image : images) {
            String url = userInfoResponse.getImageUrls().get(index);
            Assertions.assertThat(url).isEqualTo(image.getUrl());
            index++;
        }
    }
}