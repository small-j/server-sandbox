package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private String DUPLICATE_USER = "This user is already registered";

    @Transactional
    public int register(UserRequest userRequest) {
        if (!validateDuplicateUser(userRequest))
            throw new IllegalArgumentException(DUPLICATE_USER);

        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();

        return userRepository.register(user);
    }

    private boolean validateDuplicateUser(UserRequest userRequest) {
        return userRepository.findUserByEmail(userRequest.getEmail()).isEmpty();
    }
}
