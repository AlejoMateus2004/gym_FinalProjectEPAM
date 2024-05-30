package com.gymepam.service.trainee.facade;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TraineeMapper;
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

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class TraineeFacadeService{

    private TraineeService traineeService;

    private GeneratePassword generatePassword;

    private TrainerService trainerService;

    private TraineeMapper traineeMapper;

    private TrainerMapper trainerMapper;

    private TrainingMicroService trainingMicroService;


    public ResponseEntity<AuthenticationRequest> save_Trainee(TraineeRecord.TraineeRequest traineeRequest) {
        Trainee trainee = traineeMapper.traineeRequestToTrainee(traineeRequest);
        if (trainee == null) {
            return ResponseEntity.badRequest().build();
        }
        User user = trainee.getUser();

        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainee.setUser(user);

        Trainee temp = traineeService.saveTrainee(trainee);
        if (temp == null) {
            return ResponseEntity.badRequest().build();
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return new ResponseEntity<>(newAuth, HttpStatus.CREATED);
    }

    public TraineeRecord.TraineeResponseWithTrainers getTraineeByUserUsername_(String username) {
        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        return  traineeMapper.traineeToTraineeResponseWithTrainers(trainee);
    }

    public TraineeRecord.TraineeResponseWithTrainers updateTrainee_(TraineeRecord.TraineeUpdateRequest traineeRequest) {
        Trainee trainee = traineeService.updateTrainee(traineeMapper.traineeUpdateRequestToTrainee(traineeRequest));
        return traineeMapper.traineeToTraineeResponseWithTrainers(trainee);
    }

    public ResponseEntity<GlobalModelResponse> getTraineeByUserUsernameWithTrainingParams(TrainingRecord.TraineeTrainingParamsRequest traineeRequest) {
        if (traineeRequest == null || traineeRequest.traineeUsername() == null || traineeRequest.traineeUsername().isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return a bad request response if trainer username is missing
        }

        Trainee trainee = traineeService.getTraineeByUserUsername(traineeRequest.traineeUsername());
        if (traineeRequest.trainerUsername()!= null && !traineeRequest.trainerUsername().isEmpty()) {
            Trainer trainer = trainerService.getTrainerByUserUsername(traineeRequest.trainerUsername());
            if (trainer == null) {
                return ResponseEntity.notFound().build();
            }
        }
        if (trainee == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            return trainingMicroService.getTraineeTrainingListByTrainingParams(traineeRequest);
        } catch (Exception ex) {
            log.error("Error fetching training microservice", ex);
            return ResponseEntity.badRequest().build(); // Return server error response
        }
    }

    public Set<TrainerRecord.TrainerResponse> updateTraineesTrainerList(String username, Set<String> trainer_usernames){
        try{
            Trainee trainee = traineeService.getTraineeByUserUsername(username);
            if (trainee == null) {
                return null;
            }
            Set<Trainer> trainers = trainee.getTrainerList();
            trainer_usernames.forEach(trainerUsername -> {
                Trainer trainer = trainerService.getTrainerByUserUsername(trainerUsername);
                if (trainer != null) {
                    trainers.add(trainer);
                }
            });
            trainee.setTrainerList(trainers);
            Trainee trainee_ = traineeService.updateTrainee(trainee);
            return traineeMapper.traineeToTraineeResponseWithTrainers(trainee_).trainerList();

        }catch(Exception e){
            log.error("Error updating trainee's trainer list");
        }
        return null;
    }

    public ResponseEntity<Void> updateStatus(String username, boolean isActive) {
        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        if (trainee == null) {
            return ResponseEntity.notFound().build();
        }
        User user = trainee.getUser();
        user.setIsActive(isActive);
        trainee.setUser(user);
        if (traineeService.updateTrainee(trainee) == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }

    public Set<TrainerRecord.TrainerResponse> getNotAssignedTrainersByTraineeUserUsername(String username) {
        Set<Trainer> trainerList = trainerService.getActiveTrainersNotAssignedToTrainee(username);

        return trainerList.stream()
                .map(trainerMapper::trainerToTrainerResponse)
                .collect(Collectors.toSet());
    }

    public void deleteTraineeByUserName(String username) {
        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        traineeService.deleteTrainee(trainee);
    }

}
