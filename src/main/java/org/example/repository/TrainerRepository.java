package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.entity.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerRepository {
    private EntityManager entityManager;

    public TrainerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer save(Trainer trainer){
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        return trainer.getId();
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password){
        Query query = entityManager.createQuery("select t from Trainer t left join User u  " +
                "where t.user.id = u.id and u.userName = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        Boolean isMatch = (boolean) query.getSingleResult();
        return isMatch;
    }
}
