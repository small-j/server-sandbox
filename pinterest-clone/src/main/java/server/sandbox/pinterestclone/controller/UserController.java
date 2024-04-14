package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
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

    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam(value = "id") int id) {
        return ResponseEntity.ok()
                .body(userService.getUserInfo(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> userExistError(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
