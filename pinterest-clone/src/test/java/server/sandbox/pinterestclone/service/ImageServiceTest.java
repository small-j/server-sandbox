package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Category;
import server.sandbox.pinterestclone.domain.dto.CategoryRequest;
import server.sandbox.pinterestclone.domain.dto.ImageMetaRequest;
import server.sandbox.pinterestclone.domain.dto.ImageMetaResponse;
import server.sandbox.pinterestclone.repository.CategoryRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
class ImageServiceTest {

    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void addImage() throws FileNotFoundException {
        String filePath = getClass().getClassLoader().getResource("test.jpg").getPath();
        InputStream inputStream = new FileInputStream(filePath);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> ids = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = ImageMetaRequest.builder()
                .imageData(inputStream)
                .categories(ids)
                .build();

        ImageMetaResponse res = imageService.addImage(imageMetaRequest);
        Assertions.assertThat(imageRepository.findById(res.getId())).isNotNull();

        Stream<Category> categories = categoryNames.stream().map(categoryName -> categoryRepository.findByName(categoryName).get(0));
        Assertions.assertThat(categories.count()).isEqualTo(categoryNames.size());
    }
}