package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.ImageReply;
import server.sandbox.pinterestclone.domain.User;
import server.sandbox.pinterestclone.domain.dto.ImageReplyRequest;
import server.sandbox.pinterestclone.repository.ImageReplyRepository;
import server.sandbox.pinterestclone.repository.ImageRepository;
import server.sandbox.pinterestclone.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageReplyService {

    private final ImageReplyRepository imageReplyRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    private String NOT_EXIST_REPLY = "Reply does not exist.";
    private String NOT_EXIST_USER = "This user is not exist.";
    private String NOT_EXIST_IMAGE = "This image is not exist.";

    public int addReply(ImageReplyRequest imageReplyRequest) {
        User user = userRepository.findById(imageReplyRequest.getUserId());
        Image image = imageRepository.findById(imageReplyRequest.getImageMetaId());

        validateUser(user);
        validateImage(image);

        ImageReply imageReply = ImageReply.builder()
                .image(image)
                .user(user)
                .content(imageReplyRequest.getContent())
                .build();

        imageReplyRepository.addReply(imageReply);
        return imageReply.getId();
    }

    public int deleteReply(int id) {
        ImageReply imageReply = imageReplyRepository.findById(id);
        isExistReply(imageReply);

        imageReplyRepository.deleteReply(imageReply);

        return id;
    }

    private void isExistReply(ImageReply imageReply) {
        if (ObjectUtils.isEmpty(imageReply))
            throw new IllegalArgumentException(NOT_EXIST_REPLY);
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
