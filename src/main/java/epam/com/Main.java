package epam.com;

import epam.com.config.AppConfig;
import epam.com.dtos.TraineeCreateDto;
import epam.com.dtos.UserCreateDto;
import epam.com.entity.Trainer;
import epam.com.entity.TrainingType;
import epam.com.repository.TrainerRepository;
import epam.com.repository.TrainingTypeRepository;
import epam.com.service.TraineeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;
import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TraineeService traineeService = context.getBean(TraineeService.class);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

    }
}