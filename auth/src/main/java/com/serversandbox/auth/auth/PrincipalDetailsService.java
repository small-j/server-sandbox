package com.serversandbox.auth.auth;

import com.serversandbox.auth.model.User;
import com.serversandbox.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

// http://localhost:8080/login 호출되면 원래 formlogin 사용시에 loadUserByUsername 메서드가 동작한다.
// 하지만 우리는 formlogin을 사용하지 않는다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
