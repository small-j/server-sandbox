package server.sandbox.pinterestclone.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import server.sandbox.pinterestclone.domain.Image;

import java.util.List;

@Getter
@AllArgsConstructor
public class ImageDetailInfoResponse {
    @JsonProperty(value = "image")
    ImageMetaResponse imageMetaResponse;
    List<ImageReplyResponse> imageReplies;

    public static ImageDetailInfoResponse of(Image image, List<ImageReplyResponse> imageReplyResponses) {
        return new ImageDetailInfoResponse(ImageMetaResponse.of(image), imageReplyResponses);
    }
}
