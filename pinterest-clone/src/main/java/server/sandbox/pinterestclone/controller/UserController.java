package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.service.UserService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Integer> register(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok()
                .body(userService.register(userRequest));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> userExistError(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
