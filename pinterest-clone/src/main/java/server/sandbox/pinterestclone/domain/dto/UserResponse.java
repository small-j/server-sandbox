package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {
    int id;
    String name;
    String email;
}
