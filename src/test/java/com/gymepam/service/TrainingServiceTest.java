package com.gymepam.service;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.training.TrainingServiceFeignImpl;
import com.gymepam.service.training.feignClients.TrainingFeignClient;
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
    private TrainingFeignClient trainingFeignImpl;

    @InjectMocks
    private TrainingMicroService trainingService =  new TrainingServiceFeignImpl();
    private TrainingRecord.TrainingMicroserviceRequest trainingRequest;

    @BeforeEach
    void setUp() {
        trainingRequest = new TrainingRecord.TrainingMicroserviceRequest(
                "Trainer",
                "Username",
                "trainee.username",
                true,
                "trainer.username",
                "TRAININIG1",
                LocalDate.parse("2024-05-11"),
                2L
        );
    }

    @Test
    void saveTraining_Success() {
        // Setup
        when(trainingFeignImpl.saveTraining(trainingRequest)).thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity responseEntity = trainingService.saveTraining(trainingRequest);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void saveTraining_Failure() {
        // Setup
        when(trainingFeignImpl.saveTraining(trainingRequest)).thenThrow(new RuntimeException("Feign client error"));

        // Test
        ResponseEntity responseEntity = trainingService.saveTraining(trainingRequest);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void updateTrainingStatus_Success() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignImpl.updateTrainingStatusToCompleted(trainingId)).thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity responseEntity = trainingService.updateTrainingStatusToCompleted(trainingId);

        // Verify
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void updateTrainingStatus_Failure() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignImpl.updateTrainingStatusToCompleted(trainingId)).thenThrow(new RuntimeException("Feign client error"));

        // Test
        ResponseEntity responseEntity = trainingService.updateTrainingStatusToCompleted(trainingId);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void getTrainerMonthlySummary_Success() {
        // Setup
        String trainerUsername = "trainer.username";

        Map<Integer, Map<String, Long>> trainerSummaryMap = new HashMap<>();
        Map<String, Long> monthDurationMap = new HashMap<>();
        monthDurationMap.put("AUGUST", 3L);
        trainerSummaryMap.put(2022, monthDurationMap);
        TrainingRecord.TrainingSummary trainerSummary = new TrainingRecord.TrainingSummary(
                "trainer.username",
                "Trainer",
                "Username",
                true,
                trainerSummaryMap
        );

        when(trainingFeignImpl.getTrainingSummaryByTrainerUsername(any()))
                .thenReturn(ResponseEntity.ok(trainerSummary));

        // Test
        GlobalModelResponse response = trainingService.getTrainingSummaryByTrainerUsername(trainerUsername).getBody();
        assertNotNull(response);
        TrainingRecord.TrainingSummary responseSummary = (TrainingRecord.TrainingSummary) response.getResponse();
        // Verify
        assertNotNull(responseSummary);
        assertEquals(trainerSummary, responseSummary);
    }

    @Test
    void getTrainerMonthlySummary_Failure() {
        // Setup
        String trainerUsername = "example_trainer";
        when(trainingFeignImpl.getTrainingSummaryByTrainerUsername(trainerUsername))
                .thenReturn(ResponseEntity.badRequest().build());

        // Test
        ResponseEntity<GlobalModelResponse> response = trainingService.getTrainingSummaryByTrainerUsername(trainerUsername);

        // Verify
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
    @Test
    void deleteTrainingById_Success() {
        // Setup
        Long trainingId = 123L;
        when(trainingFeignImpl.deleteTrainingById(trainingId))
                .thenReturn(ResponseEntity.ok().build());

        // Test
        ResponseEntity<GlobalModelResponse> response = trainingService.deleteTrainingById(trainingId);

        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Training Deleted", Objects.requireNonNull(response.getBody()).getResponse());
    }

    @Test
    void deleteTrainingById_Failure() {
        // Setup
        Long trainingId = 123L;
        GlobalModelResponse mockResponse = new GlobalModelResponse();
        mockResponse.setMessage("Training is completed, can't be deleted");
        when(trainingFeignImpl.deleteTrainingById(trainingId))
                .thenReturn(ResponseEntity.badRequest().build());

        // Test
        ResponseEntity<GlobalModelResponse> response = trainingService.deleteTrainingById(trainingId);

        // Verify
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Training is completed, can't be deleted", Objects.requireNonNull(response.getBody()).getMessage());
    }
}