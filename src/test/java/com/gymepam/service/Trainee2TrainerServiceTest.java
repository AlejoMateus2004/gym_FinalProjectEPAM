package com.gymepam.service;

import com.gymepam.dao.Trainee2TrainerRepo;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Trainee2TrainerServiceTest {

    @Mock
    Trainee2TrainerRepo trainee2TrainerRepo;

    @InjectMocks
    private Trainee2TrainerService trainee2TrainerService;

    private Trainee2Trainer trainee2Trainer;

    @BeforeEach
    void setUp() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Juan");
        user.setLastName("Perez");
        user.setUserName("juan.perez");
        user.setPassword("JuanPerez1");
        user.setIsActive(true);
        trainer.setUser(user);


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


        trainee2Trainer = new Trainee2Trainer();
        trainee2Trainer.setTrainee(trainee);
        trainee2Trainer.setTrainer(trainer);

    }
    @DisplayName("Test, save a trainee2Trainer object")
    @Test
    void testSave() {
        when(trainee2TrainerRepo.save(trainee2Trainer)).thenReturn(trainee2Trainer);
        Trainee2Trainer expectedValue = trainee2TrainerService.save(trainee2Trainer);
        assertNotNull(expectedValue);
        assertEquals(trainee2Trainer, expectedValue);
    }
    @DisplayName("Test, get a list of trainee2Trainer by trainee")
    @Test
    void testGetTrainerListByTrainee() {
        Trainee trainee = trainee2Trainer.getTrainee();
        when(trainee2TrainerRepo.findTrainee2TrainerByTrainee(trainee)).thenReturn(Arrays.asList(trainee2Trainer));
        List<Trainee2Trainer> trainee2TrainerList = trainee2TrainerService.getTrainerListByTrainee(trainee);
        assertNotNull(trainee2TrainerList);
        assertEquals(trainee2Trainer, trainee2TrainerList.get(0));
    }
}