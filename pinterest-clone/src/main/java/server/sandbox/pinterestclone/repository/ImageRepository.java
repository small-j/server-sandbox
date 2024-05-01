package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.UserImageHistory;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final EntityManager em;

    public void addImage(Image image) {
        em.persist(image);
    }

    public void addUserImageHistory(UserImageHistory userImageHistory) {
        em.persist(userImageHistory);
    }

    public Image findById(int id) {
        return em.find(Image.class, id);
    }

    public void deleteImage(Image image) {
        em.remove(image);
    }

    public List<Image> findImageGetSimilarCategory(List<Integer> categoryIds) {
        // TODO : native query를 사용하지 않고 아래처럼 실행할 수 있는지 알아보기
//        StringBuilder query = new StringBuilder("select a from Image a inner join ImageCategory b ON a.id = b.image"); // a.id = b.image가 제대로 실행되지 않을 것 같다.
//        categoryIds.stream().forEach(categoryId -> query.append("where b.categoryId = " + categoryId));
//        query.append("group by a.id");
//        query.append("order by count(a.id) desc, a.createdDate desc");

        StringBuilder query = new StringBuilder();
        query.append("SELECT a.image_meta_id, a.created_date, a.url, a.title, a.image_content, a.image_key, a.modified_date, a.user_id ");
        query.append("FROM image_meta as a ");
        query.append("INNER JOIN image_category as b ");
        query.append("ON a.image_meta_id = b.image_meta_id ");
        if (categoryIds.size() > 0) {
            query.append("WHERE ");
            categoryIds.stream().forEach(categoryId -> query.append("b.category_id = '" + categoryId + "' or "));
            query.delete(query.length() - 3, query.length());
        }
        query.append("GROUP BY a.image_meta_id ");
        query.append("ORDER BY COUNT(a.image_meta_id) DESC, a.created_date DESC");

        return em.createNativeQuery(query.toString(), Image.class)
                .getResultList();
    }
}
