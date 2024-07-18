package server.sandbox.pinterestclone.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
import server.sandbox.pinterestclone.service.enums.ErrorMessage;

import java.util.NoSuchElementException;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageReplyService {

    private final ImageReplyRepository imageReplyRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public int addReply(ImageReplyRequest imageReplyRequest) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(userEmail);
        Image image = imageRepository.findById(imageReplyRequest.getImageMetaId());

        validateUser(user);
        validateImage(image);

        ImageReply imageReply = ImageReply.create(image, user, imageReplyRequest.getContent());
        imageReplyRepository.addReply(imageReply);
        return imageReply.getId();
    }

    public int deleteReply(int id) {
        ImageReply imageReply = imageReplyRepository.findById(id);
        isExistReply(imageReply);

        imageReplyRepository.deleteReply(imageReply, imageReply.getUser().getEmail());

        return id;
    }

    private void isExistReply(ImageReply imageReply) {
        if (ObjectUtils.isEmpty(imageReply))
            throw new NoSuchElementException(ErrorMessage.NOT_EXIST_REPLY.getMessage());
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
