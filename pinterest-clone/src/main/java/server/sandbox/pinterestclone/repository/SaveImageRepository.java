package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;

@Repository
@RequiredArgsConstructor
public class SaveImageRepository {

    private final EntityManager em;

    public void addSaveImage(SaveImage saveImage) {
        em.persist(saveImage);
    }

    public SaveImage findById(int id) {
        return em.find(SaveImage.class, id);
    }

    public SaveImage findByUserIdAndImageID(User user, Image image) {
        return em.createQuery("select si from SaveImage as si where si.user = :user and si.image = :image", SaveImage.class)
                .setParameter("user", user)
                .setParameter("image", image)
                .getSingleResult();
    }
}
