package com.gymepam.SERVICE;

import com.gymepam.DAO.TrainerRepo;
import com.gymepam.DAO.TrainingRepo;
import com.gymepam.DOMAIN.*;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    @Qualifier("InMemoryTraining")
    private TrainingRepo trainingRepository;

    @Mock
    private UserService userService;
    @InjectMocks
    private generatePasswordImpl genPassword;
    @InjectMocks
    private generateUserNameImpl genUserName;
    @InjectMocks
    private TrainingService trainingService;
    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setUserName(genUserName.generateUserName(user.getFirstName(), user.getLastName()));
        user.setPassword(genPassword.generatePassword());
        user.setIsActive(true);
        trainer.setUser(user);

        Training_Type trainingType = new Training_Type();
        trainingType.setId(new Long(1));
        trainingType.setTrainingTypeName("Weight Lifting");

        trainer.setTrainingType(trainingType);

        Trainee trainee = new Trainee();
        User user2 = new User();
        user2.setFirstName("Alejandro");
        user2.setLastName("Mateus");
        user2.setUserName(genUserName.generateUserName(user2.getFirstName(), user2.getLastName()));
        user2.setPassword(genPassword.generatePassword());
        user2.setIsActive(true);
        trainee.setUser(user2);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Cra 13 #1-33");

        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingDate(LocalDate.parse("2022-08-06"));
        training.setTrainingDuration(3);
        training.setTrainingName("Plan Three Months");
    }


    @DisplayName("Test save Training")
    @Test
    void saveTraining() {
        when(trainingRepository.save(training)).thenReturn(training);
        Training expectedValue = trainingService.saveTraining(training);
        assertNotNull(expectedValue);
        assertEquals(training, expectedValue);
    }
    @DisplayName("Test get Training")
    @Test
    void getTraining() {
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.ofNullable(training));
        Training expectedValue = trainingService.getTraining(training.getId());
        assertNotNull(expectedValue);
        assertEquals(training.getId(), expectedValue.getId());
    }

    @DisplayName("Test get all Trainings")
    @Test
    void getAllTrainings() {
        when(trainingRepository.findAll()).thenReturn(Arrays.asList(training));
        List<Training> allTrainings = trainingService.getAllTrainings();
        assertNotNull(allTrainings);
        assertEquals(training, allTrainings.get(0));
        assertThat(allTrainings.size()).isEqualTo(1);
    }
}