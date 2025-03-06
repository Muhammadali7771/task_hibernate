package epam.com.repository;

import jakarta.persistence.EntityManager;
import epam.com.entity.TrainingType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainingTypeRepository {
    private final EntityManager entityManager;

    public TrainingTypeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Optional<TrainingType> getTrainingTypeById(Integer id){
        TrainingType trainingType = entityManager.find(TrainingType.class, id);
        return trainingType != null ? Optional.of(trainingType) : Optional.empty();
    }
}
