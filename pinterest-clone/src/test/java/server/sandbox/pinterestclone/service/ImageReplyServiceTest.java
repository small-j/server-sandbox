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
import server.sandbox.pinterestclone.domain.dto.ImageReplyRequest;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.ImageReplyRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;

import java.util.NoSuchElementException;

@SpringBootTest
@Transactional
class ImageReplyServiceTest {

    @Autowired
    private ImageReplyService imageReplyService;
    @Autowired
    private ImageReplyRepository imageReplyRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void addReply() {
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

        int userId = userService.register(userRequest);
        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), "");
        int id = imageReplyService.addReply(imageReplyRequest);
        Assertions.assertThat(imageReplyRepository.findById(id)).isNotNull();
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

        int userId = userService.register(userRequest);
        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(tempId, "");
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class, () -> imageReplyService.addReply(imageReplyRequest));
    }

    @Test
    void deleteReply() {
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

        int userId = userService.register(userRequest);
        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), "");
        int id = imageReplyService.addReply(imageReplyRequest);

        Assertions.assertThat(imageReplyRepository.findById(id)).isNotNull();
        imageReplyService.deleteReply(id);
        Assertions.assertThat(imageReplyRepository.findById(id)).isNull();
    }

    @Test
    void deleteNotExistReply() {
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

        int userId = userService.register(userRequest);
        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageRepository.addImage(image);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), "");
        int id = imageReplyService.addReply(imageReplyRequest);

        Assertions.assertThat(imageReplyRepository.findById(id)).isNotNull();
        imageReplyService.deleteReply(id);

        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> imageReplyService.deleteReply(id));
    }
}