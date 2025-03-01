package epam.com.repository;

import epam.com.entity.Training;
import epam.com.entity.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class TrainingRepository {
    private final EntityManager entityManager;

    public TrainingRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer save(Training training) {
        entityManager.getTransaction().begin();
        entityManager.persist(training);
        entityManager.getTransaction().commit();
        return training.getId();
    }

    public List<Training> getTraineeTrainingsListByTraineeUsernameAndCriteria(@NonNull String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingType trainingType) {
        String sql = "select t from Training t where t.trainee.user.userName = :traineeUsername ";
        if (fromDate != null) {
            sql = sql + " and t.trainingDate > :fromDate ";
        }
        if (toDate != null) {
            sql = sql + " and t.trainingDate < :toDate";
        }
        if (trainerName != null) {
            sql = sql + " and t.trainer.user.userName = :trainerName";
        }
        if (trainingType != null) {
            sql = sql + " and t.trainingType.trainingTypeName = :trainingType";
        }
        TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
        query.setParameter("traineeUsername", traineeUsername);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (trainerName != null) {
            query.setParameter("trainerName", trainerName);
        }
        if (trainingType != null) {
            query.setParameter("trainingType", trainingType.getTrainingTypeName());
        }
        List<Training> trainings = query.getResultList();
        return trainings;
    }

    public List<Training> getTrainerTrainingsListByTrainerUsernameAndCriteria(@NonNull String trainerUsername, Date fromDate, Date toDate, String traineeName) {
        String sql = "select t from Training t where t.trainer.user.userName = :trainerUsername";
        if (fromDate != null) {
            sql = sql + " and t.trainingDate > :fromDate ";
        }
        if (toDate != null) {
            sql = sql + " and t.trainingDate < :toDate ";
        }
        if (traineeName != null) {
            sql = sql + " and t.trainee.user.userName = :traineeName";
        }
        TypedQuery<Training> query = entityManager.createQuery(sql, Training.class);
        query.setParameter("trainerUsername", trainerUsername);
        if (fromDate != null) {
            query.setParameter("fromDate", fromDate);
        }
        if (toDate != null) {
            query.setParameter("toDate", toDate);
        }
        if (traineeName != null) {
            query.setParameter("traineeName", traineeName);
        }
        List<Training> trainings = query.getResultList();
        return trainings;
    }
}
