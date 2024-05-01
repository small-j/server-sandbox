package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.domain.Category;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.*;
import server.sandbox.pinterestclone.repository.CategoryRepository;
import server.sandbox.pinterestclone.repository.ImageCategoryRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.SaveImageRepository;

import java.io.*;
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
    private ImageCategoryRepository imageCategoryRepository;
    @Autowired
    private SaveImageService saveImageService;
    @Autowired
    private SaveImageRepository saveImageRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ImageReplyService imageReplyService;

    // TODO : 외부 의존성 포함해서 테스트할 방법 찾기.
//    @Test
    /*
     * S3에 실제 이미지 업로드, 삭제 로직은 주석 처리한 후 이 테스트를 실행할 수 있다.
     * */
    void uploadImage() throws FileNotFoundException, IOException {
        String filePath = getClass().getClassLoader().getResource("test.jpg").getPath();
        FileInputStream inputStream = new FileInputStream(filePath);

        byte arr[] = inputStream.readAllBytes();
        ImageResponse imageResponse = imageService.uploadImage(new FileRequest(inputStream, "image/jpeg", arr.length));
        Assertions.assertThat(imageResponse.getUrl()).isNotNull();
    }

    // TODO : 외부 의존성 포함해서 테스트할 방법 찾기.
    // @Test
    /*
     * S3에 실제 이미지 업로드, 삭제 로직은 주석 처리한 후 이 테스트를 실행할 수 있다.
     * */
    void deleteS3Image() throws FileNotFoundException, IOException {
        String filePath = getClass().getClassLoader().getResource("test.jpg").getPath();
        FileInputStream inputStream = new FileInputStream(filePath);

        byte arr[] = inputStream.readAllBytes();
        ImageResponse imageResponse = imageService.uploadImage(new FileRequest(inputStream, "image/jpeg", arr.length));
        Assertions.assertThat(imageResponse.getUrl()).isNotNull();
    }

    @Test
    void addImage() throws FileNotFoundException, IOException {
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        int userId = userService.register(userRequest);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> ids = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

//        String filePath = getClass().getClassLoader().getResource("test.jpg").getPath();
//        FileInputStream inputStream = new FileInputStream(filePath);
//
//        byte arr[] = inputStream.readAllBytes();
//        ImageResponse imageResponse = imageService.uploadImage(new ImageRequest(inputStream, arr.length));

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest(userId, "test", "test", "", "", ids);

        int id = imageService.addImage(imageMetaRequest);
        Stream<Category> categories = categoryNames.stream().map(categoryName -> categoryRepository.findByName(categoryName).get(0));

        Assertions.assertThat(imageRepository.findById(id)).isNotNull();
        Assertions.assertThat(categories.count()).isEqualTo(categoryNames.size());
    }

    // TODO : 외부 의존성 포함해서 테스트할 방법 찾기.
//    @Test
    /*
    * S3에 실제 이미지 업로드, 삭제 로직은 주석 처리한 후 이 테스트를 실행할 수 있다.
    * */
    void deleteImage() {
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        int userId = userService.register(userRequest);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> ids = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest(userId, "test", "test", "", "", ids);
        int id = imageService.addImage(imageMetaRequest);
        Image image = imageRepository.findById(id);
        saveImageService.addSaveImage(new SaveImageRequest(userId, id));

        imageService.deleteImage(id);
        Assertions.assertThat(imageRepository.findById(id)).isNull();
        Assertions.assertThat(imageCategoryRepository.findByImage(image).size()).isEqualTo(0);
        Assertions.assertThat(saveImageRepository.findByImage(image).size()).isEqualTo(0);
        Assertions.assertThat(categoryRepository.getCategories().size()).isEqualTo(2);
    }


    @Test
    void findImage() {
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest(userId1, "test", "test", "", "", categoryIds);
        ImageMetaRequest similarCategoryImageMetaRequest1 = new ImageMetaRequest(userId1, "test", "test", "", "", List.of(categoryIds.get(0)));
        ImageMetaRequest similarCategoryImageMetaRequest2 = new ImageMetaRequest(userId1, "test", "test", "", "", List.of(categoryIds.get(0)));

        int imageId = imageService.addImage(imageMetaRequest);
        int similarCategoryImageId1 = imageService.addImage(similarCategoryImageMetaRequest1);
        int similarCategoryImageId2 = imageService.addImage(similarCategoryImageMetaRequest2);

        Image image = imageRepository.findById(imageId);
        User user = image.getUser();

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), userId2, "");
        imageReplyService.addReply(imageReplyRequest);
        imageReplyService.addReply(imageReplyRequest);

        ImageDetailInfoResponse imageDetailInfoResponse = imageService.findImage(imageId, -1);

        imageService.findImage(similarCategoryImageId1, -1);
        imageService.findImage(similarCategoryImageId2, -1);

        Assertions.assertThat(imageDetailInfoResponse.getImageMetaResponse().getTitle()).isEqualTo(image.getTitle());
        Assertions.assertThat(imageDetailInfoResponse.getImageReplies().size()).isEqualTo(2);
        Assertions.assertThat(user.getUserImageHistories().size()).isEqualTo(0);
        Assertions.assertThat(imageDetailInfoResponse.getMoreImages().size()).isEqualTo(2);

        List<Integer> similarCategoryIds = imageDetailInfoResponse.getMoreImages().stream().map(similarCategoryImage -> similarCategoryImage.getId()).toList();
        Assertions.assertThat(similarCategoryIds).contains(similarCategoryImageId1);
        Assertions.assertThat(similarCategoryIds).contains(similarCategoryImageId2);
    }

    @Test
    void addUserImageHistory() {
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password("1234")
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest(userId1, "test", "test", "", "", categoryIds);
        int imageId = imageService.addImage(imageMetaRequest);
        Image image = imageRepository.findById(imageId);
        User user = image.getUser();

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), userId2, "");
        imageReplyService.addReply(imageReplyRequest);
        imageReplyService.addReply(imageReplyRequest);

        ImageDetailInfoResponse imageDetailInfoResponse = imageService.findImage(imageId, userId1);
        Assertions.assertThat(imageDetailInfoResponse.getImageMetaResponse().getTitle()).isEqualTo(image.getTitle());
        Assertions.assertThat(imageDetailInfoResponse.getImageReplies().size()).isEqualTo(2);
        Assertions.assertThat(user.getUserImageHistories().size()).isEqualTo(1);
    }
}