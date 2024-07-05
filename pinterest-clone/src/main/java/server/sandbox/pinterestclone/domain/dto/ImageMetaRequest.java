package server.sandbox.pinterestclone.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMetaRequest {
    int userId;
    String title;
    String content;
    String key;
    String url;
    List<Integer> categoryIds;
}
