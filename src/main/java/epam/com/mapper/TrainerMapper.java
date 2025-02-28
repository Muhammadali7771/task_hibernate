package epam.com.mapper;

import epam.com.dtos.TrainerCreateDto;
import epam.com.dtos.UserCreateDto;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    public Trainer toEntity(TrainerCreateDto dto){
        UserCreateDto userCreateDto = dto.userCreateDto();

        User user = new User();
        user.setLastName(userCreateDto.lastName());
        user.setFirstName(userCreateDto.firstName());

        Trainer trainer = new Trainer();
        trainer.setSpecialization(dto.specialization());
        trainer.setUser(user);

        return trainer;
    }
}
