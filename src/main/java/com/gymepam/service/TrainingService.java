package com.gymepam.service;

import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingRequest;
import com.gymepam.service.feignClients.TrainingFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingService {

    private TrainingFeignClient trainingFeignClient;

    public ResponseEntity saveTraining(TrainingRequest trainingRequest) {
        try{
            return trainingFeignClient.saveTraining(trainingRequest);
        } catch (Exception e) {
            log.error("Error, trying to save Training", e);
            return ResponseEntity.badRequest().body("Error, trying to save Training");
        }
    }

    public ResponseEntity updateTrainingStatus(Long trainingId) {
        try{
            return trainingFeignClient.updateTrainingStatusToCompleted(trainingId);
        } catch (Exception e) {
            log.error("Error, trying to update Training status to completed", e);
            return ResponseEntity.badRequest().body("Error, trying to update Training status to completed");
        }
    }

    public TrainingSummary getTrainerMonthlySummary(String trainerUsername){
        return trainingFeignClient.getTrainingSummaryByTrainerUsername(trainerUsername).getBody();
    }

    public ResponseEntity<String> deleteTrainingById(Long trainingId) {
        if(trainingFeignClient.deleteTrainingById(trainingId).getStatusCode().equals(HttpStatus.OK)){
            return ResponseEntity.ok("Training Deleted");
        }
        return ResponseEntity.badRequest().body("Training not Deleted");

    }

}
