package epam.com.mapper;

import epam.com.dtos.TrainingCreateDto;
import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.Training;
import epam.com.service.TraineeService;
import epam.com.service.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    public Training toDto(TrainingCreateDto dto) {
        if (dto == null){
            return null;
        }
        Training training = new Training();
        if (dto.trainingName() != null){
            training.setTrainingName(dto.trainingName());
        }
        if (dto.traineeUsername() != null){
            Trainee trainee = traineeService.selectTraineeByUsername(dto.traineeUsername());
            training.setTrainee(trainee);
        }
        if (dto.trainerUsername() != null){
            Trainer trainer = trainerService.selectTraineeByUsername(dto.trainerUsername());
            training.setTrainer(trainer);
        }
        if (dto.trainingDate() != null){
            training.setTrainingDate(dto.trainingDate());
        }
        if (dto.duration() != null){
            training.setDuration(dto.duration());
        }
        return training;
    }
}
