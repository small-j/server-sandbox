package com.serversandbox.auth.controller;

import com.serversandbox.auth.model.JoinRequestDto;
import com.serversandbox.auth.model.User;
import com.serversandbox.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @GetMapping("/users")
    public List<User> users() {
        return userRepository.findAll();
    }

    @GetMapping("/category")
    public String categories() {
        return "you can get category list";
    }

    @PostMapping("/join")
    public String join(@RequestBody JoinRequestDto joinRequestDto) {
        User user = User.builder()
                .username(joinRequestDto.getUsername())
                .password(bCryptPasswordEncoder.encode(joinRequestDto.getPassword()))
                .roles("USER")
                .build();

        userRepository.save(user);
        return "회원가입완료";
    }
}
