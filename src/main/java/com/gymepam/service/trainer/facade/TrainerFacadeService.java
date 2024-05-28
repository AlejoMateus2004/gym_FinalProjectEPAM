package com.gymepam.service.trainer.facade;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.util.GeneratePassword;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerFacadeService{


    private GeneratePassword generatePassword;
    private TrainerMapper trainerMapper;
    private TrainingMicroService trainingMicroService;
    private TrainerService trainerService;
    private TraineeService traineeService;

    public ResponseEntity<AuthenticationRequest> save_Trainer(TrainerRecord.TrainerRequest trainerRequest) {
        Trainer trainer = trainerMapper.trainerRequestToTrainer(trainerRequest);
        User user = trainer.getUser();

        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainer.setUser(user);

        Trainer temp = trainerService.saveTrainer(trainer);
        if (temp == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return new ResponseEntity<>(newAuth, HttpStatus.CREATED);
    }

    public TrainerRecord.TrainerResponseWithTrainees getTrainerByUserUsername_(String username) {
        Trainer trainer = trainerService.getTrainerByUserUsername(username);
        return  trainerMapper.trainerToTrainerResponseWithTrainees(trainer);

    }

    public TrainerRecord.TrainerResponseWithTrainees updateTrainer_(TrainerRecord.TrainerUpdateRequest trainerRequest) {
        Trainer trainer_ = trainerService.updateTrainer(trainerMapper.trainerUpdateRequestToTrainer(trainerRequest));
        return trainerMapper.trainerToTrainerResponseWithTrainees(trainer_);
    }

    public ResponseEntity<GlobalModelResponse> getTrainerByUserUsernameWithTrainingParams(TrainingRecord.TrainerTrainingParamsRequest trainerRequest) {
        if (trainerRequest == null || trainerRequest.trainerUsername() == null || trainerRequest.trainerUsername().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return a bad request response if trainer username is missing
        }

        Trainer trainer = trainerService.getTrainerByUserUsername(trainerRequest.trainerUsername());
        if (trainerRequest.traineeUsername() != null && !trainerRequest.traineeUsername().isEmpty()) {
            Trainee trainee = traineeService.getTraineeByUserUsername(trainerRequest.traineeUsername());
            if (trainee == null) {
                return ResponseEntity.notFound().build();
            }
        }
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return trainingMicroService.getTrainerTrainingListByTrainingParams(trainerRequest);
        } catch (Exception ex) {
            log.error("Error fetching training microservice", ex);
            return ResponseEntity.badRequest().build(); // Return server error response
        }
    }


    public ResponseEntity updateStatus(String username, boolean isActive) {
        Trainer trainer = trainerService.getTrainerByUserUsername(username);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        User user = trainer.getUser();
        user.setIsActive(isActive);
        trainer.setUser(user);
        if (trainerService.updateTrainer(trainer) == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }
}
