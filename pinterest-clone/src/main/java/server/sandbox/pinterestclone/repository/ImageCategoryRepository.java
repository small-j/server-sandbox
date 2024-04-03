package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.ImageCategory;

@Repository
@RequiredArgsConstructor
public class ImageCategoryRepository {

    private final EntityManager em;

    public void addImageCategory(ImageCategory imageCategory) {
        em.persist(imageCategory);
    }
}
