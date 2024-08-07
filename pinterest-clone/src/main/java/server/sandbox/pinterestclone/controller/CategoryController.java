package server.sandbox.pinterestclone.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import server.sandbox.pinterestclone.domain.dto.CategoryRequest;
import server.sandbox.pinterestclone.domain.dto.CategoryResponse;
import server.sandbox.pinterestclone.service.CategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Integer> addCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok()
                .body(categoryService.addCategory(categoryRequest));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        return ResponseEntity.ok()
                .body(categoryService.getCategories());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(Exception ex){
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
