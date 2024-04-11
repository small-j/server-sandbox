package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    public void addImage(Image image) {
        em.persist(image);
    }

    public Image findById(int id) {
        return em.find(Image.class, id);
    }

    public void deleteImage(Image image) {
        em.remove(image);
    }
}
