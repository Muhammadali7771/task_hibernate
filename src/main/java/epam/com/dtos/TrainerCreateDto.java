package epam.com.dtos;

import epam.com.entity.TrainingType;

public record TrainerCreateDto(UserCreateDto userCreateDto,
                               TrainingType specialization) {
}
