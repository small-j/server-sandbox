package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
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

    public void deleteReply(ImageReply reply) {
        em.remove(reply);
    }
}
