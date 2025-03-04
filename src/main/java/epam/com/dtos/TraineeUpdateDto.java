package epam.com.dtos;

import java.util.Date;

public record TraineeUpdateDto(UserUpdateDto userUpdateDto,
                               Date dateOfBirth,
                               String address) {
}
