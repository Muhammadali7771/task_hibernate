package epam.com.dtos;

import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.TrainingType;

import java.util.Date;

public record TrainingCreateDto(String traineeUsername,
                                String trainerUsername,
                                String trainingName,
                                Date trainingDate,
                                Number duration) {
}
