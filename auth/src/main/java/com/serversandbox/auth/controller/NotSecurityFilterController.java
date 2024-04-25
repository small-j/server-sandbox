package com.serversandbox.auth.controller;

import com.serversandbox.auth.jwt.JwtProvider;
import com.serversandbox.auth.model.JoinRequestDto;
import com.serversandbox.auth.model.LoginRequestDto;
import com.serversandbox.auth.model.User;
import com.serversandbox.auth.model.dto.JwtTokenHeaderForm;
import com.serversandbox.auth.model.dto.UserInfoDto;
import com.serversandbox.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/api")
@RequiredArgsConstructor
public class NotSecurityFilterController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final String MISMATCHED_PASSWORD = "password not matched";
    private final String NON_EXIST_USER = "password not matched";

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

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println("로그인 시작?");
        User user = userRepository.findByUsername(loginRequestDto.getUsername());
        isExistUser(user);
        isEqualPassword(loginRequestDto.getPassword(), user.getPassword());
        System.out.println("password check 완료");

        UserInfoDto userInfoDto = UserInfoDto.of(user);
        String jwtToken = jwtProvider.createJwtToken(userInfoDto);
        JwtTokenHeaderForm jwtTokenHeaderForm = jwtProvider.getJwtTokenHeaderForm(jwtToken);

        return ResponseEntity.ok()
                .header(jwtTokenHeaderForm.getHeaderName(), jwtTokenHeaderForm.getJwtToken())
                .body("로그인 성공");
    }

    private void isExistUser(User user) {
        if (ObjectUtils.isEmpty(user))
            throw new IllegalArgumentException(NON_EXIST_USER);
    }

    private void isEqualPassword(String dbPassword, String requestPassword) {
        if (bCryptPasswordEncoder.matches(requestPassword, dbPassword))
            throw new BadCredentialsException(MISMATCHED_PASSWORD);
    }
}
