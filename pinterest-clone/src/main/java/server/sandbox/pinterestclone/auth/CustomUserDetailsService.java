package server.sandbox.pinterestclone.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String username) {
        // username 대신 이메일 사용.
        User user = userRepository.findUserByEmail(username);
        return new CustomUserDetails(user);
    }
}
