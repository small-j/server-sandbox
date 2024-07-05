package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.SaveImageRequest;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.SaveImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;
import server.sandbox.pinterestclone.service.enums.ErrorMessage;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SaveImageRepository saveImageRepository;

    @Transactional
    public int addSaveImage(SaveImageRequest saveImageRequest) {
        User user = userRepository.findById(saveImageRequest.getUserId());
        Image image = imageRepository.findById(saveImageRequest.getImageMetaId());

        validateUser(user);
        validateImage(image);

        SaveImage saveImage = SaveImage.createSaveImage(user, image);

        try {
            saveImageRepository.addSaveImage(saveImage);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException(ErrorMessage.DUPLICATE_SAVE_IMAGE.getMessage());
        }

        return saveImage.getId();
    }

    @Transactional
    public int deleteSaveImage(int saveImageId) {
        isExistSaveImage(saveImageId);
        // TODO: save image 조회해서 deleteSaveImage 메서드에 넘기는 방식으로 변경하기.
        saveImageRepository.deleteSaveImage(saveImageId);

        return saveImageId;
    }

    private void isExistSaveImage(int saveImageId) {
        if (ObjectUtils.isEmpty(saveImageRepository.findById(saveImageId)))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_SAVE_IMAGE.getMessage());
    }

    private void validateUser(User user) {
        if (ObjectUtils.isEmpty(user))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_USER.getMessage());
    }

    private void validateImage(Image image) {
        if (ObjectUtils.isEmpty(image))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_IMAGE.getMessage());
    }
}
