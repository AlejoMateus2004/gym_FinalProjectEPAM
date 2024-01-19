package com.gymepam.service.util;

import com.gymepam.domain.Roles;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.User;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class usernamePasswordMatchingImplTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private ValidatePassword validatePassword;

    @InjectMocks
    private UsernamePasswordMatchingImpl usernamePasswordMatching;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName("alejandro.mateus");
        user.setPassword("xljLK1Q821");
        user.setIsActive(true);
    }

    @DisplayName("Test to get User by valid username and password with Role")
    @Test
    void getUserByValidUsernameAndPasswordAndRole() {
        String username = "alejandro.mateus";
        String password = "xljLK1Q821";

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(validatePassword.validatePassword(user, password)).thenReturn(true);

        User expectedUser = usernamePasswordMatching.getUserByUsernameAndPassword(username, password, Roles.TRAINEE);
        assertEquals(user, expectedUser);
    }

    @DisplayName("Test to get User by valid username and invalid password with Role")
    @Test
    void getUserByValidUsernameAndInvalidPassword() {
        String username = "alejandro.mateus";
        String password = "xljLK1Q821";

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(validatePassword.validatePassword(user, password)).thenReturn(false);

        assertNull(usernamePasswordMatching.getUserByUsernameAndPassword(username, password, Roles.TRAINEE));
    }

    @DisplayName("Test to get User by invalid username")
    @Test
    void getUserByInvalidUsername() {
        String username = "alejandro.mateus";
        String password = "xljLK1Q821";

        when(trainerService.getTrainerByUserUsername(username)).thenReturn(null);
        assertNull(usernamePasswordMatching.getUserByUsernameAndPassword(username, password, Roles.TRAINER));
    }
}
