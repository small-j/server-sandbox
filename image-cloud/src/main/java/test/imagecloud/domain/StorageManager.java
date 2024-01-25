package test.imagecloud.domain;

import test.imagecloud.dto.FileDataDto;
import test.imagecloud.dto.FileMetaDataDto;

public interface StorageManager {
    String uploadFile(String fileName, FileMetaDataDto fileMetaDataDto, FileDataDto fileDataDto);
}
