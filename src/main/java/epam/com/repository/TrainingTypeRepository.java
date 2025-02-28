package epam.com.repository;

import jakarta.persistence.EntityManager;
import epam.com.entity.TrainingType;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeRepository {
    private final EntityManager entityManager;

    public TrainingTypeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer save(TrainingType trainingType){
        entityManager.getTransaction().begin();
        entityManager.persist(trainingType);
        entityManager.getTransaction().commit();
        return trainingType.getId();
    }
}
