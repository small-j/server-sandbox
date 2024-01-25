package test.imagecloud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileMetaDataDto {
    private Long memberId;
    private String contentType;
    private Integer contentLength;
}
