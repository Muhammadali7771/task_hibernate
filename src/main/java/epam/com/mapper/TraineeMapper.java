package epam.com.mapper;

import epam.com.dtos.TraineeCreateDto;
import epam.com.dtos.UserCreateDto;
import epam.com.entity.Trainee;
import epam.com.entity.User;
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
