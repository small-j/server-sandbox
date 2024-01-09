package test.imagecloud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import test.imagecloud.domain.ImageMetaData;

import java.util.List;

@AllArgsConstructor
@Getter
public class ImagePathListDto {

    private String imagePath;

    public ImagePathListDto(ImageMetaData imageMetaDatas) {
        imagePath = imageMetaDatas.getImagePath();
    }
}
