package com.gymepam.SERVICE;

import com.gymepam.DAO.TrainerRepo;
import com.gymepam.DOMAIN.Trainer;
import com.gymepam.DOMAIN.Training_Type;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    @Qualifier("InMemoryTrainer")
    private TrainerRepo trainerRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private generatePasswordImpl genPassword;
    @InjectMocks
    private generateUserNameImpl genUserName;
    @InjectMocks
    private TrainerService trainerService;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = new Trainer();
        User user = new User();
        user.setFirstName("Alejandro");
        user.setLastName("Mateus");
        user.setUserName(genUserName.generateUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(genPassword.generatePassword());
        user.setIsActive(true);
        trainer.setUser(user);

        Training_Type trainingType = new Training_Type();
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
}