package epam.com.dtos;

import java.util.Date;

public record TraineeCreateDto(Date dateOfBirth,
                               String address,
                               UserCreateDto userCreateDto) {
}
