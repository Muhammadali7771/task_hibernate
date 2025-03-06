package epam.com.repository;

import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class TraineeRepository {
    private final EntityManager entityManager;

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Trainee save(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();
        log.info("Trainee saved successfully: {}", trainee);
        return trainee;
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password) {
        log.info("Checking if username: {} and password match", username);
        Query query = entityManager.createQuery("select count(t) > 0 from Trainee t left join User u " +
                " on t.user.id = u.id where u.userName = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        boolean isMatch = (boolean) query.getSingleResult();
        log.debug("Username and password match result: {}", isMatch);
        return isMatch;
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        log.info("Fetching trainee by username: {}", username);
        try {
            TypedQuery<Trainee> query = entityManager.createQuery("select t from Trainee t left join User u " +
                    " on t.user.id = u.id where u.userName = :username", Trainee.class);
            query.setParameter("username", username);
            Trainee trainee = query.getSingleResult();
            log.info("Trainee found: {}", trainee);
            return Optional.of(trainee);
        } catch (Exception e) {
            log.error("Error fetching trainee with username: {}", username, e);
            return Optional.empty();
        }
    }

 //   @Transactional
    public void changePassword(String username, String newPassword) {
        log.info("Changing password for username: {}", username);
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                update User u set u.password = :password
                where u.userName = :username
                """);
        query.setParameter("password", newPassword);
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.flush();
        entityManager.clear();
        entityManager.getTransaction().commit();
        log.info("Password changed successfully for username: {}", username);
    }

   // @Transactional
    public Trainee update(Trainee trainee) {
        log.info("Updating trainee: {}", trainee);
        entityManager.getTransaction().begin();
        entityManager.merge(trainee);
        entityManager.getTransaction().commit();
        log.info("Trainee updated successfully: {}", trainee);
        return trainee;
    }

    public void activateOrDeactivateTrainee(String username, boolean isActive) {
        log.info("Activating/deactivating trainee with username: {}. Status: {}", username, isActive);
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                select u from User u left join Trainee t 
                on u.id =t.user.id where u.userName = :username
                """);
        query.setParameter("username", username);
        User user = (User) query.getSingleResult();
        user.setActive(isActive);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
        log.info("Trainee with username: {} has been {}.", username, isActive ? "activated" : "deactivated");
    }

    //@Transactional
    public void deleteByUsername(String username) {
        log.info("Deleting trainee with username: {}", username);
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("delete from Trainee t " +
                " where t.user.id in (select u.id from User u where u.userName = :username)");
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
        log.info("Trainee with username: {} deleted successfully", username);
    }

    public void updateTraineeTrainerList(String traineeUsername, List<String> trainersUsernameList) {
        log.info("Updating trainers list for trainee with username: {}", traineeUsername);
        Query query = entityManager.createQuery("""
                select t from Trainee t left join User u on u.id = t.user.id
                where u.userName = :traineeUsername
                """);
        query.setParameter("traineeUsername", traineeUsername);
        Trainee trainee = (Trainee) query.getSingleResult();
        List<Trainer> trainers = new ArrayList<>();
        for (int i = 0; i < trainersUsernameList.size(); i++) {
            Query query2 = entityManager.createQuery("""
                    select t from Trainer t left join User u on u.id = t.user.id
                    where u.userName = :trainerUsername
                    """);
            query2.setParameter("trainerUsername", trainersUsernameList.get(i));
            Trainer trainer = (Trainer) query2.getSingleResult();
            trainers.add(trainer);
        }
        trainee.setTrainers(trainers);
        entityManager.merge(trainee);
    }

    public Optional<Trainee> getTraineeById(Integer traineeId){
        log.info("Fetching trainee by ID: {}", traineeId);
        Trainee trainee = entityManager.find(Trainee.class, traineeId);
        return trainee != null ? Optional.of(trainee) : Optional.empty();
    }
}
