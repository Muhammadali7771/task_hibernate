package epam.com.service;

import epam.com.dtos.TrainerCreateDto;
import epam.com.dtos.TrainerUpdateDto;
import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import epam.com.exception.ResourceNotFoundException;
import epam.com.mapper.TrainerMapper;
import epam.com.repository.TraineeRepository;
import epam.com.repository.TrainerRepository;
import epam.com.util.UsernamePasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UsernamePasswordGenerator usernamePasswordGenerator;
    private final TraineeRepository traineeRepository;

    public Trainer create(TrainerCreateDto dto) {
        Trainer trainer = trainerMapper.toEntity(dto);
        User user = trainer.getUser();
        String username = usernamePasswordGenerator.generateUsername(user);
        user.setUserName(username);
        String password = usernamePasswordGenerator.generatePassword();
        user.setPassword(password);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return savedTrainer;
    }

    public boolean checkIfUsernameAndPasswordMatching(String username, String password) {
        return trainerRepository.checkUsernameAndPasswordMatch(username, password);
    }

    public Trainer selectTraineeByUsername(String username) {
        return trainerRepository.getTrainerByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
    }

    public Trainer update(TrainerUpdateDto dto, Integer trainerId) {
        Trainer trainer = trainerRepository.getTrainerById(trainerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        Trainer trainerUpdated = trainerMapper.partialUpdate(dto, trainer);
        return trainerRepository.update(trainerUpdated);
    }

    public void activateOrDeactivateTrainer(String username, boolean isActive){
        Trainer trainer = trainerRepository.getTrainerByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));
        trainerRepository.activateOrDeactivateTrainee(username, isActive);
    }

    public List<Trainer> getTrainersListNotAssignedOnTrainee(String traineeUsername){
        Trainee trainee = traineeRepository.getTraineeByUsername(traineeUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        List<Trainer> notAssignedTrainers = trainerRepository.getTrainersListThatNotAssignedOnTraineeByTraineeUsername(traineeUsername);
        return notAssignedTrainers;
    }
}
