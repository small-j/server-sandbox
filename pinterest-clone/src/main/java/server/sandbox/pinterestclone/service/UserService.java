package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private String DUPLICATE_USER = "This user is already registered";

    @Transactional
    public int register(UserRequest userRequest) {
//        validateDuplicateEmail(userRequest.getEmail());

        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();
        try {
            userRepository.register(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(DUPLICATE_USER);
        }

        return user.getId();
    }

    private void validateDuplicateEmail(String email) {
        List<User> users = userRepository.findUserByEmail(email);
        if (!users.isEmpty())
            throw new IllegalArgumentException(DUPLICATE_USER);
    }
}
