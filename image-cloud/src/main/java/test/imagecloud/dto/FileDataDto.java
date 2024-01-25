package test.imagecloud.dto;

import jakarta.servlet.ServletInputStream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDataDto {
    ServletInputStream inputStream;
}
