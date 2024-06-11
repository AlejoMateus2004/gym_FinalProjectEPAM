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
        TrainingRecord.TrainingMicroserviceRequest trainingMicroserviceRequest = new TrainingRecord.TrainingMicroserviceRequest(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName(),
                trainer.getUser().getUserName(),
                trainer.getUser().getIsActive(),
                trainee.getUser().getUserName(),
                trainingRequest.trainingName(),
                trainingRequest.trainingDate(),
                trainingRequest.trainingDuration()
        );
        response.setResponse(trainingService.saveTraining(trainingMicroserviceRequest));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainer(String trainerUsername) {
        Trainer trainer = trainerService.getTrainerByUserUsername(trainerUsername);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        return trainingService.getTrainingSummaryByTrainerUsername(trainerUsername);
    }

    public ResponseEntity<GlobalModelResponse> deleteTrainingById(Long trainingId) {
        return trainingService.deleteTrainingById(trainingId);
    }

    public ResponseEntity<GlobalModelResponse> updateTrainingStatus(Long trainingId) {
        return trainingService.updateTrainingStatusToCompleted(trainingId);
    }
}
