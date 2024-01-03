package com.gymepam.service;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.Trainer;
import com.gymepam.domain.TrainingType;
import com.gymepam.domain.User;
import com.gymepam.service.util.validatePassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerRepo trainerRepository;
    @Mock
    private validatePassword valPassword;
    @InjectMocks
    private TrainerService trainerService;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        User user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName("alejandro.mateus");
        user.setPassword("Al3jO123xz");
        user.setIsActive(true);
        trainer.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(new Long(1));
        trainingType.setTrainingTypeName("Weight Lifting");

        trainer.setTrainingType(trainingType);
    }


    @DisplayName("Test save Trainer")
    @Test
    void saveTrainer() {
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        Trainer expectedValue = trainerService.saveTrainer(trainer);
        assertNotNull(expectedValue);
        assertEquals(trainer, expectedValue);
    }
    @DisplayName("Test get Trainer")
    @Test
    void getTrainer() {
        when(trainerRepository.findById(trainer.getId())).thenReturn(Optional.ofNullable(trainer));
        Trainer expectedValue = trainerService.getTrainer(trainer.getId());
        assertNotNull(expectedValue);
        assertEquals(trainer.getId(), expectedValue.getId());
    }

    @DisplayName("Test get all Trainers")
    @Test
    void getAllTrainers() {
        when(trainerRepository.findAll()).thenReturn(Arrays.asList(trainer));
        List<Trainer> allTrainers = trainerService.getAllTrainers();
        assertNotNull(allTrainers);
        assertEquals(trainer, allTrainers.get(0));
        assertThat(allTrainers.size()).isEqualTo(1);
    }

    @DisplayName("Test get Trainer by UserName")
    @Test
    void testGetTrainerByUserUsername() {
        String userName = trainer.getUser().getUserName();
        when(trainerRepository.findTrainerByUserUsername(userName)).thenReturn(trainer);
        Trainer expectedValue = trainerService.getTrainerByUserUsername(userName);
        assertNotNull(expectedValue);
        assertEquals(trainer.getId(), expectedValue.getId());
    }

    @DisplayName("Test update password with correct data")
    @Test
    void testUpdatePassword() {
        String username = "alejandro.mateus";
        String oldPassword = "Al3jO123xz";
        String newPassword = "newPass123";

        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(trainer);
        when(valPassword.validatePassword(trainer.getUser(), oldPassword)).thenReturn(true);
        when(trainerRepository.save(trainer)).thenReturn(trainer);

        Trainer result = trainerService.updatePassword(username, oldPassword, newPassword);

        assertNotNull(result);
        assertEquals(newPassword, result.getUser().getPassword());
    }
    @DisplayName("Test update password with incorrect data")
    @Test
    void testUpdatePasswordTrainerNotFound() {
        String username = "username";
        String oldPassword = "oldPass";
        String newPassword = "newPass";

        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(null);

        Trainer result = trainerService.updatePassword(username, oldPassword, newPassword);

        assertNull(result);
    }
    @DisplayName("Test update password with incorrect oldPassword")
    @Test
    void testUpdatePasswordInvalidOldPassword() {
        String username = "alejandro.mateus";
        String oldPassword = "wrongOldPass";
        String newPassword = "newPass";

        when(trainerRepository.findTrainerByUserUsername(username)).thenReturn(trainer);
        when(valPassword.validatePassword(trainer.getUser(), oldPassword)).thenReturn(false);

        Trainer result = trainerService.updatePassword(username, oldPassword, newPassword);

        assertNull(result);
    }
}