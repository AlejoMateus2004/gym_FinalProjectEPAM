package com.gymepam.service.storage;

import com.gymepam.domain.entities.*;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.TrainingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private StorageService storageService;

    List<Trainee> traineeList;
    List<Trainer> trainerList;

    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setUserName("juan.perez");
        user.setPassword("oxc5-2dsd9");
        user.setIsActive(true);
        trainer.setUser(user);

        TrainingType trainingType = new TrainingType();
        trainingType.setId(1L);
        trainingType.setTrainingTypeName("Weight Lifting");

        trainer.setTrainingType(trainingType);

        Trainee trainee = new Trainee();
        User user2 = new User();
        user2.setFirstName("Alejandro");
        user2.setLastName("Mateus");
        user2.setUserName("alejandro.mateus");
        user2.setPassword("hyz-oi0921");
        user2.setIsActive(true);
        trainee.setUser(user2);
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress("Cra 13 #1-33");

        traineeList = List.of(trainee);
        trainerList = List.of(trainer);
    }


    @Test
    void loadDbInMemory() {
        when(traineeService.getAllTrainees()).thenReturn(traineeList);
        when(trainerService.getAllTrainers()).thenReturn(trainerList);

        storageService.loadDbInMemory();

        verify(traineeService, times(1)).getAllTrainees();
        verify(trainerService, times(1)).getAllTrainers();

        Map<String, List<Object>> dbInMemory = storageService.getDbInMemory();
        assertEquals(traineeList, dbInMemory.get("Trainee"));
        assertEquals(trainerList, dbInMemory.get("Trainer"));
    }
}