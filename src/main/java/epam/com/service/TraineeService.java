package epam.com.service;

import epam.com.dtos.TraineeCreateDto;
import epam.com.dtos.TraineeUpdateDto;
import epam.com.entity.Trainee;
import epam.com.entity.User;
import epam.com.exception.ResourceNotFoundException;
import epam.com.mapper.TraineeMapper;
import epam.com.repository.TraineeRepository;
import epam.com.util.UsernamePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public Trainee create(TraineeCreateDto traineeCreateDto){
        log.info("Creating trainee with details: {}", traineeCreateDto);
        Trainee trainee = traineeMapper.toEntity(traineeCreateDto);
        User user = trainee.getUser();
        String username = usernamePasswordGenerator.generateUsername(user);
        user.setUserName(username);
        String password = usernamePasswordGenerator.generatePassword();
        user.setPassword(password);
        Trainee savedTrainee = traineeRepository.save(trainee);
        log.info("Trainee created successfully with username: {}", username);
        return savedTrainee;
    }

    public boolean checkIfUsernameAndPasswordMatching(String username, String password){
        log.info("Checking if username: {} and password match", username);
        boolean isMatch = traineeRepository.checkUsernameAndPasswordMatch(username, password);
        log.info("Username and password match result: {}", isMatch);
        return isMatch;
    }

    public Trainee selectTraineeByUsername(String username){
        log.info("Fetching trainee by username: {}", username);
        return traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> {
                    log.error("Trainee not found with username: {}", username);
                    return new RuntimeException("Trainee not found");});
    }

    public void changePassword(String username, String password){
        log.info("Changing password for username: {}", username);
        traineeRepository.changePassword(username, password);
        log.info("Password changed successfully for username: {}", username);
    }

    public Trainee update(TraineeUpdateDto dto, Integer traineeId){
        log.info("Updating trainee with ID: {}", traineeId);
        Trainee trainee = traineeRepository.getTraineeById(traineeId)
                .orElseThrow(() -> {
                    log.error("Trainee not found with ID: {}", traineeId);
                    return new ResourceNotFoundException("Trainee not found");
                });
        traineeMapper.partialUpdate(dto, trainee);
        Trainee updatedTrainee = traineeRepository.update(trainee);
        log.info("Trainee with ID: {} updated successfully", traineeId);
        return updatedTrainee;
    }

    public void activateOrDeactivateTrainee(String username, boolean isActive){
        log.info("Activating or deactivating trainee with username: {}. Status: {}", username, isActive);
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.activateOrDeactivateTrainee(username, isActive);
        log.info("Trainee with username: {} has been {}.", username, isActive ? "activated" : "deactivated");
    }

    public void deleteTraineeByUsername(String username){
        log.info("Deleting trainee with username: {}", username);
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.deleteByUsername(username);
        log.info("Trainee with username: {} deleted successfully", username);
    }

    public void updateTraineeTrainersList(String username, List<String> trainersUsernameList){
        log.info("Updating trainers list for trainee with username: {}", username);
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.updateTraineeTrainerList(username, trainersUsernameList);
        log.info("Trainers list updated for trainee with username: {}", username);
    }


}
