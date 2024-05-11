package com.gymepam.service;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.feignClients.TrainingFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingFeignClient trainingFeignClient;

    @InjectMocks
    private TrainingService trainingService;
    private TrainingRecord.TrainingRequest trainingRequest;

    @BeforeEach
    void setUp() {
        trainingRequest = new TrainingRecord.TrainingRequest(
                "trainee.username",
                "trainer.username",
                "TRAININIG1",
                LocalDate.parse("2024-05-11"),
                2L
        );
    }

    @Test
    void saveTraining_Success() {
        // Setup
        when(trainingFeignClient.saveTraining(trainingRequest)).thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity responseEntity = trainingService.saveTraining(trainingRequest);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void saveTraining_Failure() {
        // Setup
        when(trainingFeignClient.saveTraining(trainingRequest)).thenThrow(new RuntimeException("Feign client error"));

        // Test
        ResponseEntity responseEntity = trainingService.saveTraining(trainingRequest);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateTrainingStatus_Success() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignClient.updateTrainingStatusToCompleted(trainingId)).thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity responseEntity = trainingService.updateTrainingStatus(trainingId);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateTrainingStatus_Failure() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignClient.updateTrainingStatusToCompleted(trainingId)).thenThrow(new RuntimeException("Feign client error"));

        // Test
        ResponseEntity responseEntity = trainingService.updateTrainingStatus(trainingId);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getTrainerMonthlySummary_Success() {
        // Setup
        String trainerUsername = "example_trainer";

        Map<Integer, Map<String, Long>> expectedSummary = new HashMap<>();
        Map<String, Long> monthlySummary = new HashMap<>();
        monthlySummary.put("JUNE", 1L);
        expectedSummary.put(2024, monthlySummary);
        TrainingRecord.TrainingSummary trainingSummary = new TrainingRecord.TrainingSummary(expectedSummary);

        when(trainingFeignClient.getTrainingSummaryByTrainerUsername(any()))
                .thenReturn(ResponseEntity.ok(trainingSummary));

        // Test
        TrainingRecord.TrainingSummary summary = trainingService.getTrainerMonthlySummary(trainerUsername);

        // Verify
        assertNotNull(summary);
        assertEquals(expectedSummary, summary.summary());
    }

    @Test
    void getTrainerMonthlySummary_Failure() {
        // Setup
        String trainerUsername = "example_trainer";
        when(trainingFeignClient.getTrainingSummaryByTrainerUsername(trainerUsername))
                .thenReturn(ResponseEntity.notFound().build());

        // Test
        TrainingRecord.TrainingSummary summary = trainingService.getTrainerMonthlySummary(trainerUsername);

        // Verify
        assertNull(summary);
    }
    @Test
    void deleteTrainingById_Success() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignClient.deleteTrainingById(trainingId))
                .thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity<String> responseEntity = trainingService.deleteTrainingById(trainingId);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Training Deleted", responseEntity.getBody());
    }

    @Test
    void deleteTrainingById_Failure() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignClient.deleteTrainingById(trainingId))
                .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());

        // Test
        ResponseEntity<String> responseEntity = trainingService.deleteTrainingById(trainingId);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Training not Deleted", responseEntity.getBody());
    }
}