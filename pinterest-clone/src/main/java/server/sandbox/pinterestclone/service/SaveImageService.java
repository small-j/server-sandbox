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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SaveImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final SaveImageRepository saveImageRepository;

    private String DUPLICATE_SAVE_IMAGE = "This image is already saved";
    private String NOT_EXIST_SAVE_IMAGE = "Save image does not exist.";
    private String NOT_EXIST_USER = "This user is not exist.";
    private String NOT_EXIST_IMAGE = "This image is not exist.";

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
            throw new IllegalArgumentException(DUPLICATE_SAVE_IMAGE);
        }

        return saveImage.getId();
    }

    @Transactional
    public int deleteSaveImage(int saveImageId) {
        isExistSaveImage(saveImageId);
        saveImageRepository.deleteSaveImage(saveImageId);

        return saveImageId;
    }

    private void isExistSaveImage(int saveImageId) {
        if (ObjectUtils.isEmpty(saveImageRepository.findById(saveImageId)))
            throw new IllegalArgumentException(NOT_EXIST_SAVE_IMAGE);
    }

    private void validateUser(User user) {
        if (ObjectUtils.isEmpty(user))
            throw new IllegalArgumentException(NOT_EXIST_USER);
    }

    private void validateImage(Image image) {
        if (ObjectUtils.isEmpty(image))
            throw new IllegalArgumentException(NOT_EXIST_IMAGE);
    }
}
