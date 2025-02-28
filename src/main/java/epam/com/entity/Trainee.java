package epam.com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private String address;
    @OneToOne(cascade = CascadeType.PERSIST)
    private User user;
    @ManyToMany
    @JoinTable(name = "trainee_trainer",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id"))
    private List<Trainer> trainers;
    @OneToMany(mappedBy = "trainee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Training> trainings;
}
