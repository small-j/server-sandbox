package com.serversandbox.auth.model.dto;

import com.serversandbox.auth.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoDto {
    private long id;
    private String username;
    private String password;

    public static UserInfoDto of (User user) {
        return new UserInfoDto(user.getId(), user.getUsername(), user.getPassword());
    }
}
