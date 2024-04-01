package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.UserRepository;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

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
}