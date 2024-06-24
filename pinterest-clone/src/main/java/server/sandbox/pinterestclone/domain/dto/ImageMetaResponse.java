package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.sandbox.pinterestclone.domain.Image;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetaResponse {
    private int id;
    private String title;
    private String content;
    private String imageUrl;

    public static ImageMetaResponse of(Image entity) {
        return new ImageMetaResponse(entity.getId(), entity.getTitle(), entity.getContent(), entity.getUrl());
    }
}
