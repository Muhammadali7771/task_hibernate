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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TraineeRepositoryTest {
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private EntityManager entityManager;
    private Trainee trainee;
    private User user;

    @BeforeEach
    void tearDown() {
        trainee = new Trainee();
        trainee.setAddress("Toshkent");
        trainee.setDateOfBirth(new Date());
        user = new User();
        user.setLastName("Akbarov");
        user.setFirstName("Muhammadali");
        user.setUserName("Muhammadali.Akbarov");
        user.setPassword("111");
        trainee.setUser(user);
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from Trainee").executeUpdate();
        entityManager.createQuery("delete from Trainer").executeUpdate();
        entityManager.createQuery("delete from User").executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Test
    void save() {

        traineeRepository.save(trainee);

        Assertions.assertNotNull(trainee.getId());
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void checkUsernameAndPasswordMatch() {
        traineeRepository.save(trainee);

        boolean result1 = traineeRepository
                .checkUsernameAndPasswordMatch("Muhammadali.Akbarov", "111");

        boolean result2 = traineeRepository
                .checkUsernameAndPasswordMatch("Muhammadali.Akbarov", "123");

        boolean result3 = traineeRepository
                .checkUsernameAndPasswordMatch("Muhammadali123", "111");

        Assertions.assertTrue(result1);
        Assertions.assertFalse(result2);
        Assertions.assertFalse(result3);
    }

    @Test
    void getTraineeByUsername_whenTraineeExists() {
        String username = "Muhammadali.Akbarov";
        traineeRepository.save(trainee);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(username);
        Assertions.assertTrue(traineeOptional.isPresent());
    }

    @Test
    void getTraineeByUsername_whenTraineeNotExists() {
        String username = "ali.valiyev";
        traineeRepository.save(trainee);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(username);
        Assertions.assertFalse(traineeOptional.isPresent());
    }

    @Test
    void changePassword() {
        String username = "Muhammadali.Akbarov";
        String newPassword = "333";
        traineeRepository.save(trainee);

        traineeRepository.changePassword(username, newPassword);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(username);
        Assertions.assertTrue(traineeOptional.isPresent());

        Trainee traineeWithNewPassword = traineeOptional.get();
        System.out.println(traineeWithNewPassword);
        Assertions.assertEquals(newPassword, traineeWithNewPassword.getUser().getPassword());
        Assertions.assertNotEquals("111", traineeWithNewPassword.getUser().getPassword());
    }

    @Test
    void update() {
        traineeRepository.save(trainee);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername("Muhammadali.Akbarov");
        Assertions.assertTrue(traineeOptional.isPresent());

        Trainee trainee1 = traineeOptional.get();
        String newAddress = "Samarqand";
        Date newDate = new Date(103, 3, 4);
        trainee1.setAddress(newAddress);
        trainee1.setDateOfBirth(newDate);
        String newFirstName = "AAA";
        String newLastName = "BBB";
        trainee1.getUser().setFirstName(newFirstName);
        trainee1.getUser().setLastName(newLastName);
        traineeRepository.update(trainee1);

        Optional<Trainee> traineeOptional2 = traineeRepository.getTraineeByUsername("Muhammadali.Akbarov");
        Assertions.assertTrue(traineeOptional2.isPresent());

        Trainee trainee2 = traineeOptional2.get();
        System.out.println(trainee2);
        Assertions.assertEquals(newAddress, trainee2.getAddress());
        Assertions.assertEquals(newDate, trainee2.getDateOfBirth());
        Assertions.assertEquals(newFirstName, trainee2.getUser().getFirstName());
        Assertions.assertEquals(newLastName, trainee2.getUser().getLastName());
    }

    @Test
        //@Disabled
    void activateOrDeactivateTrainee() {
        traineeRepository.save(trainee);
        String username = "Muhammadali.Akbarov";
        boolean status = true;

        traineeRepository.activateOrDeactivateTrainee(username, status);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(username);
        Assertions.assertTrue(traineeOptional.isPresent());
        Trainee trainee1 = traineeOptional.get();
        Assertions.assertTrue(trainee1.getUser().isActive());
    }

    @Test
    void deleteByUsername() {
        String username = "Muhammadali.Akbarov";
        traineeRepository.save(trainee);

        traineeRepository.deleteByUsername(username);

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(username);

        Assertions.assertFalse(traineeOptional.isPresent());
    }

    @Test
    void updateTraineeTrainerList() {
        traineeRepository.save(trainee);

        entityManager.getTransaction().begin();

        Trainer trainer1 = new Trainer();
        TrainingType trainingType1 = entityManager.getReference(TrainingType.class, 1L);
        trainer1.setSpecialization(trainingType1);
        User user1 = new User();
        user1.setLastName("Sobirov");
        user1.setFirstName("Salim");
        user1.setUserName("Salim.Sobirov");
        user1.setPassword("666");
        trainer1.setUser(user1);

        Trainer trainer2 = new Trainer();
        TrainingType trainingType2 = entityManager.getReference(TrainingType.class, 2L);
        trainer2.setSpecialization(trainingType2);
        User user2 = new User();
        user2.setLastName("Hakimov");
        user2.setFirstName("Mustafo");
        user2.setUserName("Mustafo.Hakimov");
        user2.setPassword("555");
        trainer2.setUser(user2);

        entityManager.persist(trainer1);
        entityManager.persist(trainer2);
        entityManager.flush();

        String traineeUsername = "Muhammadali.Akbarov";

        traineeRepository.updateTraineeTrainerList(traineeUsername,
                List.of("Salim.Sobirov", "Mustafo.Hakimov"));

        entityManager.getTransaction().commit();

        Optional<Trainee> traineeOptional = traineeRepository.getTraineeByUsername(traineeUsername);
        Assertions.assertTrue(traineeOptional.isPresent());
        Trainee trainee1 = traineeOptional.get();
        Assertions.assertEquals(2, trainee1.getTrainers().size());
    }

    @Test
    void getTraineeById() {
        Trainee trainee1 = new Trainee();
        trainee1.setAddress("Buxoro");
        trainee1.setDateOfBirth(new Date(103, 2, 2));
        User user1 = new User();
        user1.setFirstName("Ruslan");
        user1.setLastName("Botirov");
        user1.setUserName("Ruslan.Botirov");
        user1.setPassword("123");
        trainee1.setUser(user1);

        Trainee savedTrainee = traineeRepository.save(trainee1);

        Integer id = savedTrainee.getId();
        Optional<Trainee> traineeOptional = traineeRepository.getTraineeById(id);
        Assertions.assertTrue(traineeOptional.isPresent());
    }
}