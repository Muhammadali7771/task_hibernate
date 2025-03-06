package epam.com.repository;

import epam.com.config.AppConfig;
import epam.com.entity.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainingTypeRepositoryTest {
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    @Test
    void getTrainingTypeById() {
        Optional<TrainingType> trainingTypeOptional = trainingTypeRepository.getTrainingTypeById(1);
        Assertions.assertTrue(trainingTypeOptional.isPresent());
    }
}