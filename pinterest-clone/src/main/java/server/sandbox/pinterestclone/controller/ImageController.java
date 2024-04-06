package server.sandbox.pinterestclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.sandbox.pinterestclone.domain.dto.FileRequest;
import server.sandbox.pinterestclone.domain.dto.ImageMetaRequest;
import server.sandbox.pinterestclone.domain.dto.ImageResponse;
import server.sandbox.pinterestclone.service.ImageService;

import java.io.IOException;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    private String FAIL_READ_INPUT_STREAM = "Fail to read input stream data";

    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(HttpServletRequest httpServletRequest) throws IOException {
        FileRequest imageRequest = new FileRequest(httpServletRequest.getInputStream(), httpServletRequest.getContentType(), httpServletRequest.getContentLength());

        return ResponseEntity.ok().body(imageService.uploadImage(imageRequest));
    }

    @PostMapping("/meta")
    public ResponseEntity<Integer> addImage(@RequestBody ImageMetaRequest imageMetaRequest) {
        return ResponseEntity.ok().body(imageService.addImage(imageMetaRequest));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> failToReadInputStreamData(Exception ex){
        return ResponseEntity.badRequest().body(FAIL_READ_INPUT_STREAM);
    }
}
