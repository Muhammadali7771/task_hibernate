package epam.com.repository;

import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TraineeRepository {
    private final EntityManager entityManager;

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Trainee save(Trainee trainee) {
        entityManager.getTransaction().begin();
        entityManager.persist(trainee);
        entityManager.getTransaction().commit();
        return trainee;
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password) {
        Query query = entityManager.createQuery("select count(t) > 0 from Trainer t left join User u " +
                " where t.user.id = u.id and u.userName = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        boolean isMatch = (boolean) query.getSingleResult();
        return isMatch;
    }

    public Optional<Trainee> getTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery("select t from Trainee t left join User u " +
                    " on t.user.id = u.id where u.userName = :username", Trainee.class);
            query.setParameter("username", username);
            Trainee trainee = query.getSingleResult();
            return Optional.of(trainee);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void changePassword(String username, String newPassword) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                update User u set u.password = :password 
                where u.userName = :username
                """);
        query.setParameter("password", newPassword);
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Transactional
    public Trainee update(Trainee trainee) {
        entityManager.merge(trainee);
        return trainee;
    }

    public void activateOrDeactivateTrainee(String username, boolean isActive) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                select u from User u left join Trainee t 
                on u.id =t.user.id where u.userName = :username
                """);
        User user = (User) query.getSingleResult();
        user.setActive(isActive);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }

    @Transactional
    public void deleteByUsername(String username) {
        Query query = entityManager.createQuery("delete from Trainee t " +
                " where t.user.id in (select u.id from User u where u.userName = :username)");
        query.setParameter("username", username);
        query.executeUpdate();
    }

    public void updateTraineeTrainerList(String traineeUsername, List<String> trainersUsernameList) {
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
        Trainee trainee = entityManager.find(Trainee.class, traineeId);
        return trainee != null ? Optional.of(trainee) : Optional.empty();
    }
}
