package server.sandbox.pinterestclone.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import server.sandbox.pinterestclone.domain.Image;

@Getter
@NoArgsConstructor
public class ImageMetaResponse {
    private int id;
    private String imageUrl;

    public static ImageMetaResponse of(Image entity) {
        ImageMetaResponse imageMetaResponse = new ImageMetaResponse();
        imageMetaResponse.id = entity.getId();
        imageMetaResponse.imageUrl = entity.getUrl();

        return imageMetaResponse;
    }
}
