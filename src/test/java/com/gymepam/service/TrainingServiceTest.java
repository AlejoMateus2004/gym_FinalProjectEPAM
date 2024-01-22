package com.gymepam.service;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.*;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingRepo trainingRepository;

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
        user.setUserName("juan.perez");
        user.setPassword("JuanPerez1");
        user.setIsActive(true);
        trainer.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(new Long(1));
        trainingType.setTrainingTypeName("Weight Lifting");

        trainer.setTrainingType(trainingType);

        Trainee trainee = new Trainee();
        User user2 = new User();
        user2.setFirstName("Alejandro");
        user2.setLastName("Mateus");
        user2.setUserName("alejandro.mateus");
        user2.setPassword("alejo123A");
        user2.setIsActive(true);
        trainee.setUser(user2);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Cra 13 #1-33");

        training.setTrainingType(trainingType);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingDate(LocalDate.parse("2022-08-06"));
        training.setTrainingDuration(3l);
        training.setTrainingName("Plan Three Months");
    }


    @DisplayName("Test save Training")
    @Test
    void testSaveTraining() {
        when(trainingRepository.save(training)).thenReturn(training);
        Training expectedValue = trainingService.saveTraining(training);
        assertNotNull(expectedValue);
        assertEquals(training, expectedValue);
    }
    @DisplayName("Test get Training")
    @Test
    void testGetTraining() {
        when(trainingRepository.findById(training.getId())).thenReturn(Optional.ofNullable(training));
        Training expectedValue = trainingService.getTraining(training.getId());
        assertNotNull(expectedValue);
        assertEquals(training.getId(), expectedValue.getId());
    }

    @DisplayName("Test get all Trainings")
    @Test
    void testGetAllTrainings() {
        when(trainingRepository.findAll()).thenReturn(Arrays.asList(training));
        List<Training> allTrainings = trainingService.getAllTrainings();
        assertNotNull(allTrainings);
        assertEquals(training, allTrainings.get(0));
        assertThat(allTrainings.size()).isEqualTo(1);
    }

    @Test
    void testGetAllTrainingsByTrainee() {
        String userName = "juan.perez";
        when(trainingRepository.findTrainingByTrainee(userName)).thenReturn(Arrays.asList(training));
        List<Training> allTrainings = trainingService.getAllTrainingsByTrainee(userName);
        assertNotNull(allTrainings);
        assertEquals(training, allTrainings.get(0));
        assertThat(allTrainings.size()).isEqualTo(1);
    }

    @Test
    void testGetAllTrainingsByTrainer() {
        String userName = "alejandro.mateus";
        when(trainingRepository.findTrainingByTrainer(userName)).thenReturn(Arrays.asList(training));
        List<Training> allTrainings = trainingService.getAllTrainingsByTrainer(userName);
        assertNotNull(allTrainings);
        assertEquals(training, allTrainings.get(0));
        assertThat(allTrainings.size()).isEqualTo(1);
    }
}