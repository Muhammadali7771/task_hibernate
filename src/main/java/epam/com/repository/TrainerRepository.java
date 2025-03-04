package epam.com.repository;

import epam.com.entity.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TrainerRepository {
    private final EntityManager entityManager;

    public TrainerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Trainer save(Trainer trainer){
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        return trainer;
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password){
        Query query = entityManager.createQuery("select count(t) > 0 from Trainer t left join User u " +
                " on t.user.id = u.id where u.userName = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        Boolean isMatch = (boolean) query.getSingleResult();
        return isMatch;
    }

    public Optional<Trainer> getTrainerByUsername(String username){
        try {
            TypedQuery<Trainer> query = entityManager.createQuery("select t from Trainer t left join User u " +
                    "on t.user.id = u.id where u.userName = :username", Trainer.class);
            query.setParameter("username", username);
            Trainer trainer = query.getSingleResult();
            return Optional.of(trainer);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public void changePassword(String username, String password){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                update User u set u.password = :password
                where u.userName = :username    
                """);
        query.setParameter("password", password);
        query.setParameter("username", username);
        query.executeUpdate();
        entityManager.getTransaction().commit();
    }

    @Transactional
    public Trainer update(Trainer trainer){
        entityManager.merge(trainer);
        return trainer;
    }

    public void activateOrDeactivateTrainee(String username, boolean isActive) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("""
                select u from User u left join Trainer t 
                on u.id = t.user.id where u.userName = :username
                """);
        User user = (User) query.getSingleResult();
        user.setActive(isActive);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }


    public List<Trainer> getTrainersListThatNotAssignedOnTraineeByTraineeUsername(String traineeUsername){
        String sql = "select te from Trainee te where te.user.userName = :traineeUsername";
        TypedQuery<Trainee> query = entityManager.createQuery(sql, Trainee.class);
        query.setParameter("traineeUsername", traineeUsername);
        Trainee trainee = query.getSingleResult();
        List<Trainer> assignedTrainers = trainee.getTrainers();  // assigned trainers
        String sql2 = "select tr from Trainer tr";
        TypedQuery<Trainer> query2 = entityManager.createQuery(sql2, Trainer.class);
        List<Trainer> allTrainers = query2.getResultList();
        allTrainers.removeAll(assignedTrainers);
        return allTrainers;
    }

    public Optional<Trainer> getTrainerById(Integer id){
        Trainer trainer = entityManager.find(Trainer.class, id);
        return trainer != null ? Optional.of(trainer) : Optional.empty();
    }
}
