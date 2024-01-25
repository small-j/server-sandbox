package test.imagecloud.controller;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import test.imagecloud.dto.FileDataDto;
import test.imagecloud.dto.FileMetaDataDto;
import test.imagecloud.service.FileMetaDataService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileMetaDataService fileMetaDataService;

    @PostMapping("/file/stream/{member_id}")
    public String fileStreamUpload(@PathVariable("member_id") Long value, HttpServletRequest request) throws IOException {
        FileMetaDataDto fileMetaDataDto = new FileMetaDataDto(value, request.getContentType(), request.getContentLength());
        ServletInputStream servletInputStream = request.getInputStream();
        return fileMetaDataService.uploadImage(fileMetaDataDto, new FileDataDto(servletInputStream));
    }
}
