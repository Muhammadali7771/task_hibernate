package epam.com.util;

import epam.com.entity.User;
import epam.com.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


class UsernamePasswordGeneratorTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UsernamePasswordGenerator usernamePasswordGenerator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks manually
    }

    @Test
    void generateUsername_1() {
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("valiyev");
        String username = user.getFirstName().concat(".").concat(user.getLastName());
        Mockito.when(userRepository.isUsernameTaken(username)).thenReturn(false);

        Assertions.assertEquals(username, usernamePasswordGenerator.generateUsername(user));
        Mockito.verify(userRepository, Mockito.timeout(1)).isUsernameTaken(username);
    }

    @Test
    void generateUsername_2() {
        User user = new User();
        user.setFirstName("ali");
        user.setLastName("valiyev");
        String username = user.getFirstName().concat(".").concat(user.getLastName());
        Mockito.when(userRepository.isUsernameTaken(username)).thenReturn(true);

        Assertions.assertEquals(username.concat("1"), usernamePasswordGenerator.generateUsername(user));
        Mockito.verify(userRepository, Mockito.timeout(1)).isUsernameTaken(username);
    }

    @Test
    void generatePassword() {
        String password = usernamePasswordGenerator.generatePassword();
        Assertions.assertNotNull(password);
        Assertions.assertEquals(password.length(), 10);
        for (int i = 0; i < 10; i++){
            char c = password.charAt(i);
            Assertions.assertTrue(c >= 33 && c < 127);
        }
    }
}