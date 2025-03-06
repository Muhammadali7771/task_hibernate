package epam.com.repository;

import epam.com.config.AppConfig;
import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.TrainingType;
import epam.com.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainerRepositoryTest {
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private EntityManager entityManager;
    private Trainer trainer;


    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        TrainingType trainingType = entityManager.find(TrainingType.class, 1);
        trainer.setSpecialization(trainingType);
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("John.Doe");
        user.setPassword("777");
        trainer.setUser(user);

        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Trainer").executeUpdate();
        entityManager.createQuery("delete from Trainee").executeUpdate();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    void save() {
        trainerRepository.save(trainer);

        Assertions.assertNotNull(trainer.getId());
        Assertions.assertNotNull(trainer.getUser().getId());
    }

    @Test
    void checkUsernameAndPasswordMatch() {
        trainerRepository.save(trainer);

        boolean result1 = trainerRepository.checkUsernameAndPasswordMatch("John.Doe", "777");
        boolean result2 = trainerRepository.checkUsernameAndPasswordMatch("John.Doe", "111");
        boolean result3 = trainerRepository.checkUsernameAndPasswordMatch("John", "777");
        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertFalse(result3);
    }

    @Test
    void getTrainerByUsername() {
        trainerRepository.save(trainer);

        Optional<Trainer> trainerOptional1 = trainerRepository.getTrainerByUsername("John.Doe");
        Optional<Trainer> trainerOptional2 = trainerRepository.getTrainerByUsername("John");

        Assertions.assertTrue(trainerOptional1.isPresent());
        Assertions.assertFalse(trainerOptional2.isPresent());
    }

    @Test
    void changePassword() throws InterruptedException {
        trainerRepository.save(trainer);

        String username = "John.Doe";
        String newPassword = "1111";

        trainerRepository.changePassword(username, newPassword);

        Thread.sleep(1000);
        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUsername(username);
        Assertions.assertTrue(trainerOptional.isPresent());
        Trainer trainer1 = trainerOptional.get();
        Assertions.assertEquals("1111", trainer1.getUser().getPassword());
    }

    @Test
    void update() {
        trainerRepository.save(trainer);

        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUsername("John.Doe");
        Trainer trainer1 = trainerOptional.get();
        TrainingType newTrainingType = entityManager.getReference(TrainingType.class, 2);
        trainer1.setSpecialization(newTrainingType);
        trainer1.getUser().setPassword("999");
        trainer1.getUser().setFirstName("Smith");
        trainerRepository.update(trainer1);

        Optional<Trainer> updatedTrainer = trainerRepository.getTrainerByUsername("John.Doe");
        Assertions.assertEquals(2, updatedTrainer.get().getSpecialization().getId());
        Assertions.assertEquals("Smith", updatedTrainer.get().getUser().getFirstName());
        Assertions.assertEquals("999", updatedTrainer.get().getUser().getPassword());
    }

    @Test
    void activateOrDeactivateTrainee() {
        trainerRepository.save(trainer);

        trainerRepository.activateOrDeactivateTrainee("John.Doe", true);
        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUsername("John.Doe");
        Trainer trainer1 = trainerOptional.get();
        Assertions.assertTrue(trainer1.getUser().isActive());
    }

    @Test
    void getTrainersListThatNotAssignedOnTraineeByTraineeUsername() {
        trainerRepository.save(trainer);

        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Akmal");
        user.setLastName("Tohirov");
        user.setUserName("Akmal.Tohirov");
        user.setPassword("333");
        trainee.setUser(user);
        trainee.setTrainers(List.of(trainer));
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();

        //unassigned trainers
        Trainer trainer1 = new Trainer();
        Trainer trainer2 = new Trainer();
        entityManager.getTransaction().begin();
        entityManager.persist(trainer1);
        entityManager.persist(trainer2);
        entityManager.getTransaction().commit();

        List<Trainer> unassignedTrainers = trainerRepository
                .getTrainersListThatNotAssignedOnTraineeByTraineeUsername("Akmal.Tohirov");

        Assertions.assertEquals(2, unassignedTrainers.size());
    }

    @Test
    void getTrainerById() {
        trainerRepository.save(trainer);

        Optional<Trainer> trainerOptional = trainerRepository.getTrainerById(trainer.getId());
        Assertions.assertTrue(trainerOptional.isPresent());
    }
}