package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.example.entity.Trainer;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<Trainer> getTrainerByUsername(String username){
        try {
            TypedQuery<Trainer> query = entityManager.createQuery("select t from Trainer t left join User u " +
                    "where t.user.id = u.id and u.userName = :username", Trainer.class);
            query.setParameter("username", username);
            Trainer trainer = query.getSingleResult();
            return Optional.of(trainer);
        }catch (Exception e){
            return Optional.empty();
        }
    }
}
