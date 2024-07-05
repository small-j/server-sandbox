package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.LoginInfoRequest;
import server.sandbox.pinterestclone.domain.dto.UserInfoResponse;
import server.sandbox.pinterestclone.domain.dto.UserRequest;
import server.sandbox.pinterestclone.jwt.JwtProvider;
import server.sandbox.pinterestclone.jwt.dto.JwtTokenHeaderForm;
import server.sandbox.pinterestclone.jwt.dto.UserInfo;
import server.sandbox.pinterestclone.repository.UserRepository;
import server.sandbox.pinterestclone.service.enums.ErrorMessage;
import server.sandbox.pinterestclone.service.enums.UserRole;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public int register(UserRequest userRequest) {
        User user = User.builder()
                .email(userRequest.getEmail())
                .name(userRequest.getName())
                .password(bCryptPasswordEncoder.encode(userRequest.getPassword()))
                .roles(Arrays.asList(UserRole.USER))
                .build();
        try {
            userRepository.register(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_USER.getMessage());
        }

        return user.getId();
    }

    public JwtTokenHeaderForm login(LoginInfoRequest loginInfoRequest) {
        User user = findUserByEmail(loginInfoRequest.getEmail());
        validateUser(user);
        isEqualPassword(loginInfoRequest.getPassword(), user.getPassword());

        UserInfo userInfo = UserInfo.of(user);
        String jwtToken = jwtProvider.createJwtToken(userInfo);
        return jwtProvider.getJwtTokenHeaderForm(jwtToken);
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
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_USER.getMessage());
    }

    private void isEqualPassword(String requestPassword, String dbPassword) {
        if (!bCryptPasswordEncoder.matches(requestPassword, dbPassword))
            throw new BadCredentialsException(ErrorMessage.MISMATCHED_PASSWORD.getMessage());
    }

    private User findUserByEmail(String email) {
        try {
            User user = userRepository.findUserByEmail(email);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_USER.getMessage());
        }
    }
}
