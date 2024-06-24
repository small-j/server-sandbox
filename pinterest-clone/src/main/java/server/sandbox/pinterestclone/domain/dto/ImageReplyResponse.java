package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import server.sandbox.pinterestclone.domain.ImageReply;
import server.sandbox.pinterestclone.domain.User;

@Getter
@AllArgsConstructor
public class ImageReplyResponse {
    int replyId;
    String replyContent;
    int userId;
    String userName;

    public static ImageReplyResponse of(ImageReply imageReply) {
        User user = imageReply.getUser();
        return new ImageReplyResponse(imageReply.getId(), imageReply.getContent(), user.getId(), user.getName());
    }
}
