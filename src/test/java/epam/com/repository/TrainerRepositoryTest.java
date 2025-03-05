package epam.com.repository;

import epam.com.config.AppConfig;
import epam.com.entity.Trainer;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

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

    }

    @Test
    void save() {

    }

    @Test
    void checkUsernameAndPasswordMatch() {
    }

    @Test
    void getTrainerByUsername() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void update() {
    }

    @Test
    void activateOrDeactivateTrainee() {
    }

    @Test
    void getTrainersListThatNotAssignedOnTraineeByTraineeUsername() {
    }

    @Test
    void getTrainerById() {
    }
}