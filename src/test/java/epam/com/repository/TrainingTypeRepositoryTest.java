package epam.com.repository;

import epam.com.config.AppConfig;
import epam.com.entity.TrainingType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AppConfig.class})
class TrainingTypeRepositoryTest {
    @Autowired
    private TrainingTypeRepository trainingTypeRepository;

    /*@Test
    void save() {
        TrainingType trainingType = new TrainingType();
        trainingType.setTrainingTypeName("football");

        Integer id = trainingTypeRepository.save(trainingType);

        Assertions.assertNotNull(id);
    }*/
}