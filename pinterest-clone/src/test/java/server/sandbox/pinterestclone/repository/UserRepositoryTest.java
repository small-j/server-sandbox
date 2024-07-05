package server.sandbox.pinterestclone.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.User;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void register() {
        User user = User.builder()
                .email("smallj")
                .name("김지윤")
                .build();

        int id = userRepository.register(user);
        Assertions.assertThat(id).isNotNull();
    }
}