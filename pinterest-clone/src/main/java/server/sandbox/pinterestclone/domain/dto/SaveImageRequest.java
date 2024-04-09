package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaveImageRequest {
    int userId;
    int imageMetaId;
}
