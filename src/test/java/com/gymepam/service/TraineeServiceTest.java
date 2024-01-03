package com.gymepam.service;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.User;
import com.gymepam.service.util.validatePassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private TraineeRepo traineeRepository;
    @Mock
    private validatePassword valPassword;
    @InjectMocks
    private TraineeService traineeService;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setId(14L);
        User user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName("alejandro.mateus");
        user.setPassword("Al3jO123xz");
        user.setIsActive(true);
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Cra 13 #1-33");
    }

    @DisplayName("Test save Trainee")
    @Test
    void testSaveTrainee() {
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        Trainee expectedValue = traineeService.saveTrainee(trainee);
        assertNotNull(expectedValue);
        assertEquals(trainee, expectedValue);
    }
    @DisplayName("Test get Trainee")
    @Test
    void testGetTrainee() {
        when(traineeRepository.findById(trainee.getId())).thenReturn(Optional.ofNullable(trainee));
        Trainee expectedValue = traineeService.getTrainee(trainee.getId());
        assertNotNull(expectedValue);
        assertEquals(trainee.getId(), expectedValue.getId());
    }

    @DisplayName("Test get all Trainees")
    @Test
    void testGetAllTrainees() {
        when(traineeRepository.findAll()).thenReturn(Arrays.asList(trainee));
        List<Trainee> allTrainees = traineeService.getAllTrainees();
        assertNotNull(allTrainees);
        assertEquals(trainee, allTrainees.get(0));
        assertThat(allTrainees.size()).isEqualTo(1);
    }
    @DisplayName("Test delete  Trainee by Id")
    @Test
    void testDeleteTrainee() {
        willDoNothing().given(traineeRepository).delete(trainee);
        traineeService.deleteTrainee(trainee);
        verify(traineeRepository,times(1)).delete(trainee);
        assertNull(traineeService.getTrainee(trainee.getId()));
    }
    @DisplayName("Test get Trainee by UserName")
    @Test
    void testGetTraineeByUserUsername() {
        String userName = trainee.getUser().getUserName();
        when(traineeRepository.findTraineeByUserUsername(userName)).thenReturn(trainee);
        Trainee expectedValue = traineeService.getTraineeByUserUsername(userName);
        assertNotNull(expectedValue);
        assertEquals(trainee.getId(), expectedValue.getId());
    }
    @DisplayName("Test delete Trainee by UserName")
    @Test
    void testDeleteByUserUserName() {
        String userName = trainee.getUser().getUserName();
        willDoNothing().given(traineeRepository).deleteByUserUserName(userName);
        traineeService.deleteByUserUserName(userName);
        verify(traineeRepository,times(1)).deleteByUserUserName(userName);
        assertNull(traineeService.getTrainee(trainee.getId()));
    }
    @DisplayName("Test update password with correct data")
    @Test
    void testUpdatePassword() {
        String username = "alejandro.mateus";
        String oldPassword = "Al3jO123xz";
        String newPassword = "newPass123";

        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(trainee);
        when(valPassword.validatePassword(trainee.getUser(), oldPassword)).thenReturn(true);
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.updatePassword(username, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(newPassword, result.getUser().getPassword());
    }
    @DisplayName("Test update password with incorrect data")
    @Test
    void testUpdatePasswordTraineeNotFound() {
        String username = "username";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(null);

        Trainee result = traineeService.updatePassword(username, oldPassword, newPassword);

        assertNull(result);
    }
    @DisplayName("Test update password with incorrect oldPassword")
    @Test
    void testUpdatePasswordInvalidOldPassword() {
        String username = "alejandro.mateus";
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass";

        when(traineeRepository.findTraineeByUserUsername(username)).thenReturn(trainee);
        when(valPassword.validatePassword(trainee.getUser(), oldPassword)).thenReturn(false);

        Trainee result = traineeService.updatePassword(username, oldPassword, newPassword);

        assertNull(result);
    }
}