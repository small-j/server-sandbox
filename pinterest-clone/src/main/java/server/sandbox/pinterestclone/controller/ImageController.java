package server.sandbox.pinterestclone.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.sandbox.pinterestclone.domain.dto.*;
import server.sandbox.pinterestclone.service.ImageService;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<ImageResponse> uploadImage(HttpServletRequest httpServletRequest) throws IOException {
        FileRequest imageRequest = new FileRequest(httpServletRequest.getInputStream(), httpServletRequest.getContentType(), httpServletRequest.getContentLength());

        return ResponseEntity.ok().body(imageService.uploadImage(imageRequest));
    }

    @PostMapping("/meta")
    public ResponseEntity<Integer> addImage(@RequestBody ImageMetaRequest imageMetaRequest) {
        return ResponseEntity.ok().body(imageService.addImage(imageMetaRequest));
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteImage(@RequestParam(value = "id") int id) {
        return ResponseEntity.ok()
                .body(imageService.deleteImage(id));
    }

    @GetMapping
    public ResponseEntity<ImageDetailInfoResponse> findImage(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "user_id", required = false, defaultValue = "-1") int userId
    ) {
        return ResponseEntity.ok()
                .body(imageService.findImage(id, userId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ImageMetaSimpleResponse>> searchImage(@RequestParam(value = "search-word") String searchWord) {
        return ResponseEntity.ok()
                .body(imageService.getSearchImages(searchWord));
    }

    @GetMapping("/main")
    public ResponseEntity<List<ImageMetaSimpleResponse> > mainImage(@RequestParam(value = "user-id") int id) {
        return ResponseEntity.ok()
                .body(imageService.getMainImages(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
