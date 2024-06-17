package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Category;
import server.sandbox.pinterestclone.domain.dto.CategoryRequest;
import server.sandbox.pinterestclone.domain.dto.CategoryResponse;
import server.sandbox.pinterestclone.repository.CategoryRepository;
import server.sandbox.pinterestclone.service.exception.ErrorMessage;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public int addCategory(CategoryRequest categoryRequest) {
        if (!validateDuplicateUser(categoryRequest))
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_CATEGORY_NAME.getMessage());

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
