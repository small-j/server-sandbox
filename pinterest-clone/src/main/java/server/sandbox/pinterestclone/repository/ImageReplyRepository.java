package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.ImageReply;

@Repository
@RequiredArgsConstructor
public class ImageReplyRepository {

    private final EntityManager em;

    public void addReply(ImageReply reply) {
        em.persist(reply);
    }

    public ImageReply findById(int id) {
        return em.find(ImageReply.class, id);
    }

    @PreAuthorize("isAuthenticated() and (principal.isDataOwner(#imageReplyCreatorEmail) or principal.isAdmin())")
    public void deleteReply(ImageReply reply, @P("imageReplyCreatorEmail") String imageReplyCreatorEmail) {
        em.remove(reply);
    }
}
