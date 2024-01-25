package test.imagecloud.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import test.imagecloud.domain.FileMetaData;

@AllArgsConstructor
@Getter
public class FilePathListDto {

    private String filePath;

    public FilePathListDto(FileMetaData fileMetaData) {
        filePath = fileMetaData.getFilePath();
    }
}
