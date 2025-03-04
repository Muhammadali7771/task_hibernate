package epam.com.dtos;

import epam.com.entity.TrainingType;

public record TrainerUpdateDto(UserUpdateDto userUpdateDto,
                               TrainingType specialization) {
}
