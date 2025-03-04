package epam.com.service;

import epam.com.dtos.TrainingCreateDto;
import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.Training;
import epam.com.entity.TrainingType;
import epam.com.exception.ResourceNotFoundException;
import epam.com.mapper.TraineeMapper;
import epam.com.mapper.TrainerMapper;
import epam.com.mapper.TrainingMapper;
import epam.com.repository.TraineeRepository;
import epam.com.repository.TrainerRepository;
import epam.com.repository.TrainingRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    public void add(TrainingCreateDto dto){
        Training training = trainingMapper.toDto(dto);
        trainingRepository.save(training);
    }

    public List<Training> getTraineeTrainingsList(@NonNull String traineeUsername, Date fromDate, Date toDate, String trainerName, TrainingType trainingType){
        Trainee trainee = traineeRepository.getTraineeByUsername(traineeUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        return trainingRepository.getTraineeTrainingsListByTraineeUsernameAndCriteria(traineeUsername, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainingsList(@NonNull String trainerUsername, Date fromDate, Date toDate, String traineeName){
        Trainer trainer = trainerRepository.getTrainerByUsername(trainerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        return trainingRepository.getTrainerTrainingsListByTrainerUsernameAndCriteria(trainerUsername, fromDate, toDate, traineeName);
    }

}
