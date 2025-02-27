package org.example.dtos;

import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.TrainingType;

import java.util.Date;

public record TrainingCreateDto(Trainee trainee,
                                Trainer trainer,
                                String trainingName,
                                TrainingType trainingType,
                                Date trainingDate,
                                Number duration,
                                UserCreateDto dto) {
}
