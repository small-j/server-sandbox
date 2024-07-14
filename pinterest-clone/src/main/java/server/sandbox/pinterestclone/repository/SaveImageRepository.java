package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.SaveImage;
import server.sandbox.pinterestclone.domain.User;

import java.util.List;

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

    @PreAuthorize("isAuthenticated() and (principal.isDataOwner(#saveImageCreatorEmail) or principal.isAdmin())")
    public void deleteSaveImage(int id, @P("saveImageCreatorEmail") String saveImageCreatorEmail) {
        em.createQuery("delete from SaveImage si where si.id = :id")
                .setParameter("id", id)
                .executeUpdate();

        em.clear();
    }

    public void deleteSaveImageToImage(Image image) {
        em.createQuery("delete from SaveImage si where si.image = :image")
                .setParameter("image", image)
                .executeUpdate();
    }

    public List<SaveImage> findByImage(Image image) {
        return em.createQuery("select si from SaveImage as si where si.image = :image", SaveImage.class)
                .setParameter("image", image)
                .getResultList();
    }

    public List<SaveImage> findByUser(User user) {
        return em.createQuery("select si from SaveImage as si where si.user = :user", SaveImage.class)
                .setParameter("user", user)
                .getResultList();
    }
}
