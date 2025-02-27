package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.example.entity.Trainee;
import org.springframework.stereotype.Repository;

@Repository
public class TraineeRepository {
    private final EntityManager entityManager;

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer save(Trainee trainee) {
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();
        return trainee.getId();
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password) {
        Query query = entityManager.createQuery("select count(t) > 0 from Trainer t left join User u " +
                " where t.user.id = u.id and u.userName = :username and u.password = :password");
        boolean isMatch = (boolean) query.getSingleResult();
        return isMatch;
    }
}
