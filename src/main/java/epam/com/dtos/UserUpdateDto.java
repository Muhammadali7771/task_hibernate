package epam.com.dtos;

public record UserUpdateDto(String firstName,
                            String lastName,
                            String userName,
                            boolean isActive) {
}
