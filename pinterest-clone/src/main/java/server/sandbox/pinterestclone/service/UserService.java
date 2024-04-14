package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.repository.SaveImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SaveImageRepository saveImageRepository;

    private String DUPLICATE_USER = "This user is already registered";
    private String NOT_EXIST_USER = "This user is not exist.";

    @Transactional
    public int register(UserRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .build();
        try {
            userRepository.register(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(DUPLICATE_USER);
        }

        return user.getId();
    }

    public UserInfoResponse getUserInfo(int id) {
        User user = userRepository.findById(id);
        validateUser(user);

        // TODO: 위, 아래 중 성능이 더 좋은게 뭔지 테스트해보기
//        List<SaveImage> saveImages = saveImageRepository.findByUser(user);
        List<SaveImage> saveImages = user.getSaveImages()
                .stream()
                .filter(saveImage -> saveImage.getUser().getId() == user.getId())
                .toList();
        // 이미지 목록 조회해서 이미지 url 반환.
        List<Image> images = saveImages.stream().map(saveImage -> saveImage.getImage()).toList();

        return UserInfoResponse.of(user, images);
    }

    private void validateUser(User user) {
        if (ObjectUtils.isEmpty(user))
            throw new IllegalArgumentException(NOT_EXIST_USER);
    }
}
