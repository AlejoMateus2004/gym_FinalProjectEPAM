package com.gymepam.service.training;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import org.springframework.http.ResponseEntity;

public interface TrainingMicroService {
    ResponseEntity<GlobalModelResponse> saveTraining(TrainingRecord.TrainingMicroserviceRequest trainingRequest);
    ResponseEntity<GlobalModelResponse> updateTrainingStatusToCompleted(Long trainingId);
    ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainerUsername(String trainerUsername);
    ResponseEntity<GlobalModelResponse> deleteTrainingById(Long trainingId);
    ResponseEntity<GlobalModelResponse> getTrainerTrainingListByTrainingParams(TrainingRecord.TrainerTrainingParamsRequest trainerRequest);
    ResponseEntity<GlobalModelResponse> getTraineeTrainingListByTrainingParams(TrainingRecord.TraineeTrainingParamsRequest traineeRequest);
}
