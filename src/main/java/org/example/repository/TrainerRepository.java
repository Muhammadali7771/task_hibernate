package org.example.repository;

import jakarta.persistence.EntityManager;
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
}
