package org.example.mapper;

import org.example.dtos.TraineeCreateDto;
import org.example.dtos.UserCreateDto;
import org.example.entity.Trainee;
import org.example.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TraineeMapper {
    public Trainee toEntity(TraineeCreateDto dto){
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
}
