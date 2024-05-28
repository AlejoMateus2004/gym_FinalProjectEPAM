package com.gymepam.service.training.facade;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainerRecord.TrainerResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.TrainingRecord.TrainerDetailsTrainingSummary;
import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.training.TrainingMicroService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
@Service
public class TrainingFacadeService{

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingMicroService trainingService;
    private TrainerMapper trainerMapper;

    public ResponseEntity<GlobalModelResponse> saveTraining(TrainingRecord.TrainingRequest trainingRequest){
        GlobalModelResponse response = new GlobalModelResponse();

        Trainer trainer = trainerService.getTrainerByUserUsername(trainingRequest.trainerUsername());
        Trainee trainee = traineeService.getTraineeByUserUsername(trainingRequest.traineeUsername());
        if(trainee == null || trainer == null){
            response.setMessage("The Trainee or the Trainer does not exist!");
            return ResponseEntity.badRequest().body(response);
        }
        response.setResponse(trainingService.saveTraining(trainingRequest));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainer(String trainerUsername) {
        ResponseEntity<GlobalModelResponse> response = null;
        Trainer trainer = trainerService.getTrainerByUserUsername(trainerUsername);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        TrainerResponse trainerResponse = trainerMapper.trainerToTrainerResponse(trainer);
        response = trainingService.getTrainingSummaryByTrainerUsername(trainerUsername);
        GlobalModelResponse globalModelResponse = response.getBody();
        TrainingSummary summary = (TrainingSummary) Objects.requireNonNull(globalModelResponse).getResponse();
        if (summary == null) {
            return ResponseEntity.notFound().build();
        }
        TrainerDetailsTrainingSummary trainerDetailsTrainingSummary = new TrainerDetailsTrainingSummary(
                trainerResponse,
                summary.summary()
        );
        globalModelResponse.setResponse(trainerDetailsTrainingSummary);
        return ResponseEntity.ok(globalModelResponse);
    }

    public ResponseEntity<GlobalModelResponse> deleteTrainingById(Long trainingId) {
        return trainingService.deleteTrainingById(trainingId);
    }

    public ResponseEntity<GlobalModelResponse> updateTrainingStatus(Long trainingId) {
        return trainingService.updateTrainingStatusToCompleted(trainingId);
    }
}
