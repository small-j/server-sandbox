package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.dto.CategoryRequest;
import server.sandbox.pinterestclone.repository.CategoryRepository;

@SpringBootTest
@Transactional
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void addCategory() {
        CategoryRequest categoryRequest = new CategoryRequest(" 스누피");
        int id = categoryService.addCategory(categoryRequest);

        Assertions.assertThat(categoryRepository.findById(id)).isNotNull();
    }

    @Test
    void checkCategoryNameDuplication() {
        CategoryRequest categoryRequest1 = new CategoryRequest(" 스누피");
        CategoryRequest categoryRequest2 = new CategoryRequest(" 스누피");

        categoryService.addCategory(categoryRequest1);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(categoryRequest2));
    }
}