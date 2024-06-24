package server.sandbox.pinterestclone.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.sandbox.pinterestclone.domain.User;

@Getter
@AllArgsConstructor
public class UserInfo {
    private long id;
    private String email;
    private String password;

    public static UserInfo of (User user) {
        return new UserInfo(user.getId(), user.getEmail(), user.getPassword());
    }
}
