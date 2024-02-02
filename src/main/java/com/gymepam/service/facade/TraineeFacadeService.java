package com.gymepam.service.facade;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TraineeMapper;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.util.GeneratePassword;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class TraineeFacadeService extends TraineeService{
    GeneratePassword generatePassword;

    TrainerService trainerService;

    TraineeMapper traineeMapper;

    TrainerMapper trainerMapper;



    public ResponseEntity<AuthenticationRequest> save_Trainee(TraineeRecord.TraineeRequest traineeRequest) {
        Trainee trainee = traineeMapper.traineeRequestToTrainee(traineeRequest);
        User user = trainee.getUser();

        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainee.setUser(user);

        Trainee temp = saveTrainee(trainee);
        if (temp == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return new ResponseEntity<>(newAuth, HttpStatus.CREATED);
    }


    public TraineeRecord.TraineeResponseWithTrainers getTraineeByUserUsername_(String username) {
        Trainee trainee =getTraineeByUserUsername(username);
        return  traineeMapper.traineeToTraineeResponseWithTrainers(trainee);
    }

    public TraineeRecord.TraineeResponseWithTrainers updateTrainee_(TraineeRecord.TraineeUpdateRequest traineeRequest) {

        Trainee trainee_ = updateTrainee(traineeMapper.traineeUpdateRequestToTrainee(traineeRequest));
        TraineeRecord.TraineeResponseWithTrainers trainee = traineeMapper.traineeToTraineeResponseWithTrainers(trainee_);
        return trainee;
    }
    public List<TrainingRecord.TraineeTrainingResponse> getTraineeByUserUsernameWithTrainingParams_(TraineeRecord.TraineeRequestWithTrainingParams traineeRequest) {
        if (traineeRequest.trainee_username() == null || traineeRequest.trainee_username().isEmpty()) {
            return null;
        }
        Trainee trainee = getTraineeByUserUsernameWithTrainingParams(traineeRequest);
        TraineeRecord.TraineeResponseWithTrainings traineeResponse = traineeMapper.traineeToTraineeResponseWithTrainings(trainee);
        if (traineeResponse == null) {
            return null;
        }
        return traineeResponse.trainingList();
    }

    public Set<TrainerRecord.TrainerResponse> updateTraineesTrainerList(String username, Set<String> trainer_usernames){
        try{
            Trainee trainee = getTraineeByUserUsername(username);
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
            Trainee trainee_ = updateTrainee(trainee);
            return traineeMapper.traineeToTraineeResponseWithTrainers(trainee_).trainerList();

        }catch(Exception e){
            log.error("Error updating trainee's trainer list");
        }
        return null;
    }

    public ResponseEntity updateStatus(String username, boolean isActive) {
        Trainee trainee = getTraineeByUserUsername(username);
        if (trainee == null) {
            return ResponseEntity.notFound().build();
        }
        User user = trainee.getUser();
        user.setIsActive(isActive);
        trainee.setUser(user);
        if (updateTrainee(trainee) == null){
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
}
