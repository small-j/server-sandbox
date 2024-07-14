package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.auth.CustomUserDetails;
import server.sandbox.pinterestclone.auth.CustomUserDetailsService;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.SaveImageRepository;

import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class SaveImageServiceTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SaveImageRepository saveImageRepository;
    @Autowired
    private SaveImageService saveImageService;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void addSaveImage() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(image.getId());
        int id = saveImageService.addSaveImage(saveImageRequest);

        Assertions.assertThat(saveImageRepository.findById(id)).isNotNull();
    }

    @Test
    void checkNotExistImage() {
        int tempId = 1;
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        SaveImageRequest saveImageRequest = new SaveImageRequest(tempId);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> saveImageService.addSaveImage(saveImageRequest));
    }

    @Test
    void checkSaveImageDuplication() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(image.getId());
        saveImageService.addSaveImage(saveImageRequest);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> saveImageService.addSaveImage(saveImageRequest));
    }

    @Test
    void deleteSaveImage() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(image.getId());
        int id = saveImageService.addSaveImage(saveImageRequest);

        saveImageService.deleteSaveImage(id);

        Assertions.assertThat(saveImageRepository.findById(id)).isNull();
    }

    @Test
    void deleteNotExistSaveImage() {
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        Image image = Image.builder()
                .title("test")
                .content("test")
                .url("test")
                .key("test")
                .build();

        userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        SaveImageRequest saveImageRequest = new SaveImageRequest(image.getId());
        int id = saveImageService.addSaveImage(saveImageRequest);

        saveImageService.deleteSaveImage(id);
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> saveImageService.deleteSaveImage(id));
    }
}