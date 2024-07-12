package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageReplyRequest {
    int imageMetaId;
    String content;
}
