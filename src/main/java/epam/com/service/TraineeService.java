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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final UsernamePasswordGenerator usernamePasswordGenerator;

    public Trainee create(TraineeCreateDto traineeCreateDto){
        Trainee trainee = traineeMapper.toEntity(traineeCreateDto);
        User user = trainee.getUser();
        String username = usernamePasswordGenerator.generateUsername(user);
        user.setUserName(username);
        String password = usernamePasswordGenerator.generatePassword();
        user.setPassword(password);
        Trainee savedTrainee = traineeRepository.save(trainee);
        return savedTrainee;
    }

    public boolean checkIfUsernameAndPasswordMatching(String username, String password){
        return traineeRepository.checkUsernameAndPasswordMatch(username, password);
    }

    public Trainee selectTraineeByUsername(String username){
        return traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found"));
    }

    public void changePassword(String username, String password){
        traineeRepository.changePassword(username, password);
    }

    public Trainee update(TraineeUpdateDto dto, Integer traineeId){
        Trainee trainee = traineeRepository.getTraineeById(traineeId)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeMapper.partialUpdate(dto, trainee);
        Trainee updatedTrainee = traineeRepository.update(trainee);
        return updatedTrainee;
    }

    public void activateOrDeactivateTrainee(String username, boolean isActive){
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.activateOrDeactivateTrainee(username, isActive);
    }

    public void deleteTraineeByUsername(String username){
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.deleteByUsername(username);
    }

    public void updateTraineeTrainersList(String username, List<String> trainersUsernameList){
        Trainee trainee = traineeRepository.getTraineeByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Trainee not found"));
        traineeRepository.updateTraineeTrainerList(username, trainersUsernameList);
    }


}
