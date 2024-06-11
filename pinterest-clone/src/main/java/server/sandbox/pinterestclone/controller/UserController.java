package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.LoginInfoRequest;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.jwt.dto.JwtTokenHeaderForm;
import server.sandbox.pinterestclone.service.UserService;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Integer> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok()
                .body(userService.register(userRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginInfoRequest loginInfoRequest) {
        JwtTokenHeaderForm jwtTokenHeaderForm = userService.login(loginInfoRequest);
        return ResponseEntity.ok()
                .header(jwtTokenHeaderForm.getHeaderName(), jwtTokenHeaderForm.getJwtToken())
                .body("로그인 성공");
    }

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam(value = "id") int id) {
        return ResponseEntity.ok()
                .body(userService.getUserInfo(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
