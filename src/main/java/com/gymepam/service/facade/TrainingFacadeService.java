package com.gymepam.service.facade;

import com.gymepam.domain.dto.records.TrainerRecord.TrainerResponse;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.domain.dto.records.TrainingRecord.TrainerDetailsTrainingSummary;
import com.gymepam.domain.dto.records.TrainingRecord;

import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.TrainingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class TrainingFacadeService{

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private TrainerMapper trainerMapper;

    public ResponseEntity saveTraining(TrainingRecord.TrainingRequest trainingRequest){
        Trainer trainer = trainerService.getTrainerByUserUsername(trainingRequest.trainerUsername());
        Trainee trainee = traineeService.getTraineeByUserUsername(trainingRequest.traineeUsername());
        if(trainee == null || trainer == null){
            return ResponseEntity.badRequest().body("The Trainee or the Trainer does not exist!");
        }
        return trainingService.saveTraining(trainingRequest);
    }

    public ResponseEntity<TrainerDetailsTrainingSummary> getTrainingSummaryByTrainer(String trainerUsername) {
        Trainer trainer = trainerService.getTrainerByUserUsername(trainerUsername);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        TrainerResponse trainerResponse = trainerMapper.trainerToTrainerResponse(trainer);
        TrainingSummary summary = trainingService.getTrainerMonthlySummary(trainerUsername);
        TrainerDetailsTrainingSummary trainerDetailsTrainingSummary = new TrainerDetailsTrainingSummary(
                trainerResponse,
                summary.summary()
        );
        return ResponseEntity.ok(trainerDetailsTrainingSummary);
    }

    public ResponseEntity<String> deleteTrainingById(Long trainingId) {
        return trainingService.deleteTrainingById(trainingId);
    }

    public ResponseEntity updateTrainingStatus(Long trainingId) {
        return trainingService.updateTrainingStatus(trainingId);
    }
}
