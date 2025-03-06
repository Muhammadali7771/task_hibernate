package epam.com;

import epam.com.config.AppConfig;
import epam.com.entity.Trainer;
import epam.com.repository.TrainerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        TrainerRepository trainerRepository = context.getBean(TrainerRepository.class);

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Optional<Trainer> trainerOptional = trainerRepository.getTrainerByUsername("John.Doe");
        Trainer trainer = trainerOptional.get();
        trainer.getUser().setPassword("00000");
        trainerRepository.update(trainer);
    }
}