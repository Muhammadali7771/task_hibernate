package epam.com.dtos;

import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.TrainingType;

import java.util.Date;

public record TrainingCreateDto(Trainee trainee,
                                Trainer trainer,
                                String trainingName,
                                TrainingType trainingType,
                                Date trainingDate,
                                Number duration,
                                UserCreateDto dto) {
}
