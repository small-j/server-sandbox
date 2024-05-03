package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.sandbox.pinterestclone.domain.Image;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetaSimpleResponse {
    private int id;
    private String url;

    public static List<ImageMetaSimpleResponse> of(List<Image> moreImages) {
        return moreImages.stream().map(image -> new ImageMetaSimpleResponse(image.getId(), image.getUrl())).toList();
    }
}
