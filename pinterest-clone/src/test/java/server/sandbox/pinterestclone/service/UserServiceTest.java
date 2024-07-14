package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.auth.CustomUserDetails;
import server.sandbox.pinterestclone.auth.CustomUserDetailsService;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.LoginInfoRequest;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.jwt.dto.JwtTokenHeaderForm;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;
import server.sandbox.pinterestclone.service.enums.UserRole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


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
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void register() {
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        int id = userService.register(userRequest);

        Assertions.assertThat(userRepository.findById(id)).isNotNull();
    }

    @Test
    void login() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();
        userService.register(userRequest);

        LoginInfoRequest loginInfoRequest = new LoginInfoRequest(userRequest.getEmail(), password);
        LoginInfoRequest wrongLoginInfo = new LoginInfoRequest(userRequest.getEmail(), "");
        LoginInfoRequest NonExistUser = new LoginInfoRequest("", "");
        JwtTokenHeaderForm jwtTokenHeaderForm = userService.login(loginInfoRequest);

        Assertions.assertThat(jwtTokenHeaderForm.getJwtToken()).isNotNull();
        org.junit.jupiter.api.Assertions.assertThrows(BadCredentialsException.class, () -> userService.login(wrongLoginInfo));
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> userService.login(NonExistUser));
    }

    @Test
    void checkEmailDuplication() {
        UserRequest user1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("김지윤")
                .password("1234")
                .build();

        UserRequest user2 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("김지윤")
                .password("1234")
                .build();

        userService.register(user1);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> userService.register(user2));
    }

    @Test
    void getUserInfo() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
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

        int id = userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        images.stream().forEach(image -> imageRepository.addImage(image));

        images.stream().forEach(image -> saveImageService.addSaveImage(new SaveImageRequest(image.getId())));

        UserInfoResponse userInfoResponse = userService.getUserInfo(id);
        Assertions.assertThat(userInfoResponse.getUserResponse().getEmail()).isEqualTo(userRequest.getEmail());
        Assertions.assertThat(userInfoResponse.getImageUrls().size()).isEqualTo(images.size());

        int index = 0;
        for (Image image : images) {
            String url = userInfoResponse.getImageUrls().get(index);
            Assertions.assertThat(url).isEqualTo(image.getUrl());
            index++;
        }
    }
}