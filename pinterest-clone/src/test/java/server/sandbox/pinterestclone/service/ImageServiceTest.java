package server.sandbox.pinterestclone.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import server.sandbox.pinterestclone.auth.CustomUserDetails;
import server.sandbox.pinterestclone.auth.CustomUserDetailsService;
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
import java.util.NoSuchElementException;
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
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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
        String password = "1234";
        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        int userId = userService.register(userRequest);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> ids = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();
        List<Integer> notExistCategoryIds = new ArrayList<>();
        notExistCategoryIds.add(1984892);

//        String filePath = getClass().getClassLoader().getResource("test.jpg").getPath();
//        FileInputStream inputStream = new FileInputStream(filePath);
//
//        byte arr[] = inputStream.readAllBytes();
//        ImageResponse imageResponse = imageService.uploadImage(new ImageRequest(inputStream, arr.length));

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", ids);
        ImageMetaRequest notExistCategoryImageMetaRequest = new ImageMetaRequest("test", "test", "", "", notExistCategoryIds);

        int id = imageService.addImage(imageMetaRequest);
        Stream<Category> categories = categoryNames.stream().map(categoryName -> categoryRepository.findByName(categoryName).get(0));

        Assertions.assertThat(imageRepository.findById(id)).isNotNull();
        Assertions.assertThat(categories.count()).isEqualTo(categoryNames.size());
        org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class
                , () -> imageService.addImage(notExistCategoryImageMetaRequest));
    }

    // TODO : 외부 의존성 포함해서 테스트할 방법 찾기.
//    @Test
    /*
    * S3에 실제 이미지 업로드, 삭제 로직은 주석 처리한 후 이 테스트를 실행할 수 있다.
    * */
    void deleteImage() {
        String password = "1234";
        UserRequest ownerUserRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest userRequest = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest adminUserRequest = UserRequest.builder()
                .email("admin@gmail.com")
                .name("admin")
                .password(password)
                .build();
        // admin 유저에게 어떻게 ADMIN 권한을 부여하지?

        int userId = userService.register(ownerUserRequest);
        userService.register(userRequest);
        userService.register(adminUserRequest);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> ids = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", ids);
        int id = imageService.addImage(imageMetaRequest);
        Image image = imageRepository.findById(id);
        saveImageService.addSaveImage(new SaveImageRequest(userId, id));

        // TODO: 추후 테스트 방법 찾아보기.
//        // SecurityContextHolder에 Authentication 객체 담기.
//        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest.getEmail());
//        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        org.junit.jupiter.api.Assertions.assertThrows(AccessDeniedException.class
//                , () -> imageService.deleteImage(id));

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(ownerUserRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        imageService.deleteImage(id);
        Assertions.assertThat(imageRepository.findById(id)).isNull();
        Assertions.assertThat(imageCategoryRepository.findByImage(image).size()).isEqualTo(0);
        Assertions.assertThat(saveImageRepository.findByImage(image).size()).isEqualTo(0);
        Assertions.assertThat(categoryRepository.getCategories().size()).isEqualTo(2);
    }


    @Test
    void findImage() {
        String password = "1234";
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest1.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", categoryIds);
        ImageMetaRequest similarCategoryImageMetaRequest1 = new ImageMetaRequest("test", "test", "", "", List.of(categoryIds.get(0)));
        ImageMetaRequest similarCategoryImageMetaRequest2 = new ImageMetaRequest("test", "test", "", "", List.of(categoryIds.get(0)));

        int imageId = imageService.addImage(imageMetaRequest);
        int similarCategoryImageId1 = imageService.addImage(similarCategoryImageMetaRequest1);
        int similarCategoryImageId2 = imageService.addImage(similarCategoryImageMetaRequest2);

        Image image = imageRepository.findById(imageId);
        User user = image.getUser();

        // SecurityContextHolder에 Authentication 객체 담기.
        customUserDetails = customUserDetailsService.loadUserByUsername(userRequest2.getEmail());
        authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), "");
        imageReplyService.addReply(imageReplyRequest);
        imageReplyService.addReply(imageReplyRequest);

        ImageDetailInfoResponse imageDetailInfoResponse = imageService.findImage(imageId, -1);

        // TODO : 아래 코드 없이도 테스트 성공하나?
//        imageService.findImage(similarCategoryImageId1, -1);
//        imageService.findImage(similarCategoryImageId2, -1);

        Assertions.assertThat(imageDetailInfoResponse.getImageMetaResponse().getTitle()).isEqualTo(image.getTitle());
        Assertions.assertThat(imageDetailInfoResponse.getImageReplies().size()).isEqualTo(2);
        Assertions.assertThat(user.getUserImageHistories().size()).isEqualTo(0);
        Assertions.assertThat(imageDetailInfoResponse.getMoreImages().size()).isEqualTo(2);

        List<Integer> similarCategoryIds = imageDetailInfoResponse.getMoreImages().stream().map(similarCategoryImage -> similarCategoryImage.getId()).toList();
        Assertions.assertThat(similarCategoryIds).contains(similarCategoryImageId1);
        Assertions.assertThat(similarCategoryIds).contains(similarCategoryImageId2);
    }

    @Test
    void getMainImages() {
        String password = "1234";
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest1.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        // 같은 카테고리를 가진 이미지 3개 생성.
        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", categoryIds);
        ImageMetaRequest similarCategoryImageMetaRequest1 = new ImageMetaRequest("test", "test", "", "", List.of(categoryIds.get(0)));
        ImageMetaRequest similarCategoryImageMetaRequest2 = new ImageMetaRequest("test", "test", "", "", List.of(categoryIds.get(0)));

        imageService.addImage(imageMetaRequest);
        int similarCategoryImageId1 = imageService.addImage(similarCategoryImageMetaRequest1);
        imageService.addImage(similarCategoryImageMetaRequest2);

        // 이미지 조회. -> image history 기록됨.
        imageService.findImage(similarCategoryImageId1, userId1);

        List<ImageMetaSimpleResponse> imageMetaSimpleResponse1 = imageService.getMainImages(userId1);
        List<ImageMetaSimpleResponse> imageMetaSimpleResponse2 = imageService.getMainImages(userId2);

        Assertions.assertThat(imageMetaSimpleResponse1.size()).isEqualTo(3);
        Assertions.assertThat(imageMetaSimpleResponse2.size()).isEqualTo(3);
    }

    @Test
    void getSearchImages() {
        String password = "1234";
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest1.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        // 같은 카테고리를 가진 이미지 3개 생성.
        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", categoryIds);
        ImageMetaRequest similarCategoryImageMetaRequest1 = new ImageMetaRequest("test", "test", "", "", List.of(categoryIds.get(0)));
        ImageMetaRequest similarCategoryImageMetaRequest2 = new ImageMetaRequest("스누피 이미지", "", "", "", List.of(categoryIds.get(0)));

        imageService.addImage(imageMetaRequest);
        imageService.addImage(similarCategoryImageMetaRequest1);
        imageService.addImage(similarCategoryImageMetaRequest2);

        List<ImageMetaSimpleResponse> imageMetaSimpleResponses1 = imageService.getSearchImages("스누피");
        List<ImageMetaSimpleResponse> imageMetaSimpleResponses2 = imageService.getSearchImages("test");

        Assertions.assertThat(imageMetaSimpleResponses1.size()).isEqualTo(3);
        Assertions.assertThat(imageMetaSimpleResponses2.size()).isEqualTo(2);
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class
                , () -> imageService.getSearchImages(""));
    }

    @Test
    void addUserImageHistory() {
        String password = "1234";
        UserRequest userRequest1 = UserRequest.builder()
                .email("smallj@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        UserRequest userRequest2 = UserRequest.builder()
                .email("aa@gmail.com")
                .name("jiyun")
                .password(password)
                .build();

        int userId1 = userService.register(userRequest1);
        int userId2 = userService.register(userRequest2);

        // SecurityContextHolder에 Authentication 객체 담기.
        CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(userRequest1.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> categoryNames = new ArrayList<>() {
            {
                add("스누피");
                add("찰리 브라운");
            }
        };
        Stream<CategoryRequest> categoryRequestStream = categoryNames.stream().map(name -> new CategoryRequest(name));
        List<Integer> categoryIds = categoryRequestStream.map(categoryRequest -> categoryService.addCategory(categoryRequest)).toList();

        ImageMetaRequest imageMetaRequest = new ImageMetaRequest("test", "test", "", "", categoryIds);
        int imageId = imageService.addImage(imageMetaRequest);
        Image image = imageRepository.findById(imageId);
        User user = image.getUser();

        // SecurityContextHolder에 Authentication 객체 담기.
        customUserDetails = customUserDetailsService.loadUserByUsername(userRequest2.getEmail());
        authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ImageReplyRequest imageReplyRequest = new ImageReplyRequest(image.getId(), "");
        imageReplyService.addReply(imageReplyRequest);
        imageReplyService.addReply(imageReplyRequest);

        ImageDetailInfoResponse imageDetailInfoResponse = imageService.findImage(imageId, userId1);
        Assertions.assertThat(imageDetailInfoResponse.getImageMetaResponse().getTitle()).isEqualTo(image.getTitle());
        Assertions.assertThat(imageDetailInfoResponse.getImageReplies().size()).isEqualTo(2);
        Assertions.assertThat(user.getUserImageHistories().size()).isEqualTo(1);
    }
}