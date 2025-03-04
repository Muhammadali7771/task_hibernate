package epam.com.mapper;

import epam.com.dtos.TraineeCreateDto;
import epam.com.dtos.TraineeUpdateDto;
import epam.com.dtos.UserCreateDto;
import epam.com.dtos.UserUpdateDto;
import epam.com.entity.Trainee;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {
    public Trainee toEntity(TraineeCreateDto dto){
        if (dto == null){
            return null;
        }
        UserCreateDto userCreateDto = dto.userCreateDto();
        User user = new User();
        user.setFirstName(userCreateDto.firstName());
        user.setLastName(userCreateDto.lastName());

        Trainee trainee = new Trainee();
        trainee.setAddress(dto.address());
        trainee.setDateOfBirth(dto.dateOfBirth());
        trainee.setUser(user);

        return trainee;
    }

    public Trainee partialUpdate(TraineeUpdateDto dto, Trainee trainee){
        UserUpdateDto userUpdateDto = dto.userUpdateDto();
        User user = trainee.getUser();
        if (userUpdateDto != null){
           if (userUpdateDto.userName() != null){
               user.setUserName(userUpdateDto.userName());
           }
           if (userUpdateDto.firstName() != null){
               user.setFirstName(userUpdateDto.firstName());
           }
           if (userUpdateDto.lastName() != null){
               user.setLastName(userUpdateDto.lastName());
           }
           user.setActive(userUpdateDto.isActive());
        }

        if (dto.address() != null){
            trainee.setAddress(dto.address());
        }
        if (dto.dateOfBirth() != null){
            trainee.setDateOfBirth(dto.dateOfBirth());
        }
        return trainee;
    }
}
