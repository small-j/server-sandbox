package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.service.SaveImageService;

@Controller
@RequestMapping("/save-image")
@RequiredArgsConstructor
public class SaveImageController {

    private final SaveImageService saveImageService;

    @PostMapping
    public ResponseEntity<Integer> addSaveImage(@RequestBody SaveImageRequest saveImageRequest) {
        return ResponseEntity.ok()
                .body(saveImageService.addSaveImage(saveImageRequest));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> saveImageExistError(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
