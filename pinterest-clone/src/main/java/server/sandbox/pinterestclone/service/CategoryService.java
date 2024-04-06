package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Category;
import server.sandbox.pinterestclone.domain.dto.CategoryRequest;
import server.sandbox.pinterestclone.domain.dto.CategoryResponse;
import server.sandbox.pinterestclone.repository.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private String DUPLICATE_CATEGORY_NAME = "This category is already existed";

    @Transactional
    public int addCategory(CategoryRequest categoryRequest) {
        if (!validateDuplicateUser(categoryRequest))
            throw new IllegalArgumentException(DUPLICATE_CATEGORY_NAME);

        Category category = Category.builder()
                .name(categoryRequest.getName())
                .build();

        categoryRepository.addCategory(category);

        return category.getId();
    }

    public List<CategoryResponse> getCategories() {
        return categoryRepository.getCategories()
                .stream()
                .map(category -> CategoryResponse.of(category))
                .toList();
    }

    private boolean validateDuplicateUser(CategoryRequest categoryRequest) {
        return categoryRepository.findByName(categoryRequest.getName()).isEmpty();
    }
}
