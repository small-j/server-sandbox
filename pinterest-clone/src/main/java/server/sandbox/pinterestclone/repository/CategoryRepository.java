package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Category;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {

    private final EntityManager em;

    public int addCategory(Category category) {
        em.persist(category);

        return category.getId();
    }


    public List<Category> findByName(String name) {
        return em.createQuery("select c from Category as c where c.name = :name", Category.class)
                .setParameter("name", name)
                .getResultList();
    }

    public Category findById(int id) {
        return em.find(Category.class, id);
    }
}
