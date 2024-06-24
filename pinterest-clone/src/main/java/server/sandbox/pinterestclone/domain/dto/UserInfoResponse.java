package server.sandbox.pinterestclone.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    @JsonProperty(value = "user")
    UserResponse userResponse;
    List<String> imageUrls;

    public static UserInfoResponse of(User user, List<Image> images) {
        UserResponse userResponse = new UserResponse(user.getId(), user.getName(), user.getEmail());
        List<String> imageUrls = images.stream().map(image -> image.getUrl()).toList();

        return new UserInfoResponse(userResponse, imageUrls);
    }
}
