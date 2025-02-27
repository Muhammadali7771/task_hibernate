package org.example.mapper;

import org.example.dtos.TraineeCreateDto;
import org.example.dtos.TrainerCreateDto;
import org.example.dtos.UserCreateDto;
import org.example.entity.Trainer;
import org.example.entity.User;
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
