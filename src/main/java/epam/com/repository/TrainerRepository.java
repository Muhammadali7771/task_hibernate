package epam.com.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TrainerRepository {
    private final EntityManager entityManager;

    public TrainerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Integer save(Trainer trainer){
        entityManager.getTransaction().begin();
        entityManager.persist(trainer);
        entityManager.getTransaction().commit();
        return trainer.getId();
    }

    public boolean checkUsernameAndPasswordMatch(String username, String password){
        Query query = entityManager.createQuery("select t from Trainer t left join User u " +
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

    public void changePassword(User user, String password){
        entityManager.getTransaction().begin();
        user.setPassword(password);
        entityManager.merge(user);
        entityManager.getTransaction().commit();
    }

    @Transactional
    public void update(Trainer trainer){
        entityManager.merge(trainer);
    }

    public void activate(Integer trainerId){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("update User u set u.isActive = true " +
                " where u.id in (select t.user.id from Trainee t where t.id = :traineeId)");
        query.setParameter("traineeId", trainerId);
        entityManager.getTransaction().commit();
    }

    public void deActivate(Integer trainerId){
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("update User u set u.isActive = false " +
                " where u.id in (select t.user.id from Trainee t where t.id = :traineeId)");
        query.setParameter("traineeId", trainerId);
        entityManager.getTransaction().commit();
    }
}
