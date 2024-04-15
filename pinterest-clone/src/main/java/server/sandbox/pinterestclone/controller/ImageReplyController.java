package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.sandbox.pinterestclone.domain.dto.ImageReplyRequest;
import server.sandbox.pinterestclone.service.ImageReplyService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ImageReplyController {

    private final ImageReplyService imageReplyService;

    @PostMapping
    public ResponseEntity<Integer> addReply(@RequestBody ImageReplyRequest imageReplyRequest) {
        return ResponseEntity.ok()
                .body(imageReplyService.addReply(imageReplyRequest));
    }

    @DeleteMapping
    public ResponseEntity<Integer> deleteReply(@RequestParam(value = "id") int id) {
        return ResponseEntity.ok()
                .body(imageReplyService.deleteReply(id));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
