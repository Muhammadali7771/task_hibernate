package epam.com.repository;

import epam.com.config.AppConfig;
import epam.com.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainingRepositoryTest {
    @Autowired
    private TrainingRepository trainingRepository;
    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Training").executeUpdate();
        entityManager.createQuery("delete from Trainee").executeUpdate();
        entityManager.createQuery("delete from Trainer ").executeUpdate();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    void save() {
        Training training = new Training();
        training.setTrainingName("new football training");
        training.setTrainingDate(new Date(126, 3, 2));
        training.setDuration(10L);
        Training savedTrainee = trainingRepository.save(training);

        Integer savedId = savedTrainee.getId();
        Training training1 = entityManager.find(Training.class, savedId);
        Assertions.assertNotNull(training1);
    }

    @Test
    void getTraineeTrainingsListByTraineeUsernameAndCriteria() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("valiyev");
        user.setUserName("ali.valiyev");
        user.setPassword("8888");
        trainee.setUser(user);
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();

        TrainingType trainingType1 = entityManager.find(TrainingType.class, 1);
        Training training1 = new Training();
        training1.setTrainingName("new football training");
        training1.setTrainingDate(new Date(126, 3, 2));
        training1.setDuration(10L);
        training1.setTrainingType(trainingType1);
        training1.setTrainee(trainee);

        trainingRepository.save(training1);

        TrainingType trainingType2 = entityManager.find(TrainingType.class, 2);
        Training training2 = new Training();
        training2.setTrainingName("new basketball training");
        training2.setTrainingDate(new Date(125, 5, 5));
        training2.setDuration(30);
        training2.setTrainee(trainee);
        training2.setTrainingType(trainingType2);
        trainingRepository.save(training2);

        List<Training> trainings = trainingRepository
                .getTraineeTrainingsListByTraineeUsernameAndCriteria("ali.valiyev", new Date(125, 5, 6), new Date(127, 6, 6), null, trainingType1);
        Assertions.assertEquals(1, trainings.size());

    }

    @Test
    void getTrainerTrainingsListByTrainerUsernameAndCriteria() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Samandar");
        user.setLastName("Jasurov");
        user.setUserName("Samandar.Jasurov");
        user.setPassword("4444");
        trainer.setUser(user);
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();

        TrainingType trainingType1 = entityManager.find(TrainingType.class, 1);
        Training training1 = new Training();
        training1.setTrainingName("football training");
        training1.setTrainingDate(new Date(126, 3, 2));
        training1.setDuration(10L);
        training1.setTrainingType(trainingType1);
        training1.setTrainer(trainer);
        trainingRepository.save(training1);

        TrainingType trainingType2 = entityManager.find(TrainingType.class, 2);
        Training training2 = new Training();
        training2.setTrainingName("basketball training");
        training2.setTrainingDate(new Date(125, 5, 5));
        training2.setDuration(30);
        training2.setTrainer(trainer);
        training2.setTrainingType(trainingType2);
        trainingRepository.save(training2);

        List<Training> trainings = trainingRepository
                .getTrainerTrainingsListByTrainerUsernameAndCriteria("Samandar.Jasurov", new Date(123, 3,3), new Date(125, 5, 10), null);
        Assertions.assertEquals(1, trainings.size());
    }
}