package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.ImageCategory;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageCategoryRepository {

    private final EntityManager em;

    public void addImageCategory(ImageCategory imageCategory) {
        em.persist(imageCategory);
    }

    public List<ImageCategory> findByImage(Image image) {
        return em.createQuery("select ic from ImageCategory as ic where ic.image = :image", ImageCategory.class)
                .setParameter("image", image)
                .getResultList();
    }
}
