package com.gymepam.service.training.feignClients;

import com.gymepam.domain.dto.records.TrainingRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TrainingFallBack implements TrainingFeignClient {
    @Override
    public ResponseEntity saveTraining(TrainingRecord.TrainingMicroserviceRequest trainingRequest) {
        log.error("saveTraining() service is not working");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity updateTrainingStatusToCompleted(Long trainingId) {
        log.error("updateTrainingStatusToCompleted() service is not working");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<List<TrainingRecord.TrainerTrainingResponse>> getTrainerTrainingListByTrainingParams(TrainingRecord.TrainerTrainingParamsRequest trainingParams) {
        log.error("getTrainerTrainingList() service is not working");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @Override
    public ResponseEntity<List<TrainingRecord.TraineeTrainingResponse>> getTraineeTrainingListByTrainingParams(TrainingRecord.TraineeTrainingParamsRequest trainingParams) {
        log.error("getTraineeTrainingList() service is not working");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @Override
    public ResponseEntity<TrainingRecord.TrainingSummary> getTrainingSummaryByTrainerUsername(String trainerUsername) {
        log.error("getTrainingSummaryByTrainerUsername() service is not working");
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<Void> deleteTrainingById(Long trainingId) {
        log.error("deleteTrainingById() service is not working");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
