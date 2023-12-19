package com.gymepam.SERVICE;

import com.gymepam.DAO.TraineeRepo;
import com.gymepam.DAO.TrainerRepo;
import com.gymepam.DOMAIN.Trainee;
import com.gymepam.DOMAIN.User;
import com.gymepam.SERVICE.UTIL.generatePasswordImpl;
import com.gymepam.SERVICE.UTIL.generateUserNameImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    @Qualifier("InMemoryTrainee")
    private TraineeRepo traineeRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private generatePasswordImpl genPassword;
    @InjectMocks
    private generateUserNameImpl genUserName;
    @InjectMocks
    private TraineeService traineeService;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        User user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName(genUserName.generateUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(genPassword.generatePassword());
        user.setIsActive(true);
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Cra 13 #1-33");
    }


    @DisplayName("Test save Trainee")
    @Test
    void saveTrainee() {
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        Trainee expectedValue = traineeService.saveTrainee(trainee);
        assertNotNull(expectedValue);
        assertEquals(trainee, expectedValue);
    }
    @DisplayName("Test get Trainee")
    @Test
    void getTrainee() {
        when(traineeRepository.findById(trainee.getId())).thenReturn(Optional.ofNullable(trainee));
        Trainee expectedValue = traineeService.getTrainee(trainee.getId());
        assertNotNull(expectedValue);
        assertEquals(trainee.getId(), expectedValue.getId());
    }

    @DisplayName("Test get all Trainees")
    @Test
    void getAllTrainees() {
        when(traineeRepository.findAll()).thenReturn(Arrays.asList(trainee));
        List<Trainee> allTrainees = traineeService.getAllTrainees();
        assertNotNull(allTrainees);
        assertEquals(trainee, allTrainees.get(0));
        assertThat(allTrainees.size()).isEqualTo(1);
    }

    @DisplayName("Test delete  Trainee by Id")
    @Test
    void deleteTrainee() {
        willDoNothing().given(traineeRepository).delete(trainee);
        traineeService.deleteTrainee(trainee);
        verify(traineeRepository,times(1)).delete(trainee);
        assertNull(traineeService.getTrainee(trainee.getId()));
    }
}