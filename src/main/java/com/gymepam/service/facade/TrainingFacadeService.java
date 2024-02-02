package com.gymepam.service.facade;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.Training;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.TrainingService;
import com.gymepam.service.util.FormatDate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
@Slf4j
@AllArgsConstructor
@Service
public class TrainingFacadeService extends TrainingService {

    TraineeService traineeService;
    TrainerService trainerService;

    FormatDate formatDate;

    public ResponseEntity saveTraining_(TrainingRecord.TrainingRequest trainingRequest) {
        try {
            Trainee trainee = traineeService.getTraineeByUserUsername(trainingRequest.traineeUsername());
            Trainer trainer = trainerService.getTrainerByUserUsername(trainingRequest.trainerUsername());

            Training newTraining = new Training();
            newTraining.setTrainingName(trainingRequest.trainingName());
            newTraining.setTrainingDate(trainingRequest.trainingDate());
            newTraining.setTrainingDuration(trainingRequest.trainingDuration());
            newTraining.setTrainee(trainee);
            newTraining.setTrainer(trainer);
            if (saveTraining(newTraining) != null) {
                log.error("Training created");
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.noContent().build();

        }catch (Exception e) {
            log.error("Error creating new Training", e);
            return ResponseEntity.badRequest().build();
        }

    }
}
