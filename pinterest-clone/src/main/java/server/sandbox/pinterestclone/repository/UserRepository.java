package server.sandbox.pinterestclone.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import server.sandbox.pinterestclone.domain.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public int register(User user) {
        em.persist(user);

        return user.getId();
    }

    public User findById(int id) {
        return em.find(User.class, id);
    }

    public User findUserByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
