package org.example.dtos;

import org.example.entity.TrainingType;

public record TrainerCreateDto(UserCreateDto userCreateDto,
                               TrainingType specialization) {
}
