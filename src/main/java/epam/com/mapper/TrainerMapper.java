package epam.com.mapper;

import epam.com.dtos.TrainerCreateDto;
import epam.com.dtos.TrainerUpdateDto;
import epam.com.dtos.UserCreateDto;
import epam.com.dtos.UserUpdateDto;
import epam.com.entity.Trainer;
import epam.com.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TrainerMapper {
    public Trainer toEntity(TrainerCreateDto dto) {
        if (dto == null) {
            return null;
        }
        UserCreateDto userCreateDto = dto.userCreateDto();

        User user = new User();
        user.setLastName(userCreateDto.lastName());
        user.setFirstName(userCreateDto.firstName());

        Trainer trainer = new Trainer();
        trainer.setSpecialization(dto.specialization());
        trainer.setUser(user);

        return trainer;
    }

    public Trainer partialUpdate(TrainerUpdateDto dto, Trainer trainer) {
        UserUpdateDto userUpdateDto = dto.userUpdateDto();
        User user = trainer.getUser();
        if (userUpdateDto != null) {
            if (userUpdateDto.firstName() != null) {
                user.setFirstName(userUpdateDto.firstName());
            }
            if (userUpdateDto.lastName() != null) {
                user.setLastName(userUpdateDto.lastName());
            }
            if (userUpdateDto.userName() != null) {
                user.setUserName(userUpdateDto.userName());
            }
            user.setActive(userUpdateDto.isActive());
        }

        if (dto.specialization() != null) {
            trainer.setSpecialization(dto.specialization());
        }
        return trainer;
    }
}
