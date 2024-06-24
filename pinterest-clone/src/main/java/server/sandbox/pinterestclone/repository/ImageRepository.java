package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.Image;
import server.sandbox.pinterestclone.domain.ImageCategory;
import server.sandbox.pinterestclone.domain.User;
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

    public List<Image> findImagesWithSimilarCategories(List<Integer> categoryIds, int imageId) {
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
        query.append("HAVING a.image_meta_id != " + imageId + " ");
        query.append("ORDER BY COUNT(a.image_meta_id) DESC, a.created_date DESC");

        return em.createNativeQuery(query.toString(), Image.class)
                .getResultList();
    }

    public List<Image> getImageFromImageHistory(User user) {
        String query = "select ui.image from UserImageHistory as ui";
        query += " where ui.user = :user";
        query += " order by ui.createdDate desc";
        query += " limit 10";
        return em.createQuery(query, Image.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Integer> getImageCategoryIdFromImages(List<Image> images) {
        String query = "select distinct ic.categoryId from ImageCategory as ic";
        if (images.size() > 0) {
            String temp = " where";
            for (int i = 0; i < images.size(); i++) {
                temp += " ic.image = :image" + i + " or";
            }

            query += temp.substring(0, temp.length() - 3);
        }

        Query q = em.createQuery(query);
        if (images.size() > 0) {
            for (int i = 0; i < images.size(); i++) {
                q.setParameter("image" + i, images.get(i));
            }
        }

        return q.getResultList();
    }

    public List<Image> getRecommendRandomImages(List<Integer> categoryIds) {
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
        query.append("ORDER BY RAND()");
        query.append("LIMIT 30");
        // TODO : 추후 30개씩 다음 데이터를 뽑는 로직으로 변경해야함. -> 30 -> 60 -> 90.

        return em.createNativeQuery(query.toString(), Image.class)
                .getResultList();
    }

    public List<Image> getImageTitleOrContentRelationalImages(String searchStr) {
        String query = "select i from Image as i";
        query += " where i.title like '%" + searchStr + "%' or i.content like '%" + searchStr + "%'";
        query += " order by i.createdDate desc";

        return em.createQuery(query, Image.class)
                .getResultList();
    }

    public List<Image> getCategoryRelationalImages(List<ImageCategory> imageCategories) {
        String query = "select i from Image as i";
        query += " inner join ImageCategory as ic";
        query += " on i = ic.image";
        if (imageCategories.size() > 0) {
            String temp = " where";
            for (ImageCategory imageCategory : imageCategories)
                temp += " ic.categoryId = " + imageCategory.getCategoryId() + " or";

            query += temp.substring(0, temp.length() - 3);
        }
        query += " group by i.id";
        query += " order by i.createdDate";

        return em.createQuery(query, Image.class)
                .getResultList();
    }
}
