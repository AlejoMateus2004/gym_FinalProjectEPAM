package com.gymepam.service.facade;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.TrainerService;
import com.gymepam.service.util.GeneratePassword;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TrainerFacadeService extends TrainerService{

    GeneratePassword generatePassword;
    TrainerMapper trainerMapper;


    public ResponseEntity<AuthenticationRequest> save_Trainer(TrainerRecord.TrainerRequest trainerRequest) {
        Trainer trainer = trainerMapper.trainerRequestToTrainer(trainerRequest);
        User user = trainer.getUser();

        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainer.setUser(user);

        Trainer temp = saveTrainer(trainer);
        if (temp == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return new ResponseEntity<>(newAuth, HttpStatus.CREATED);
    }

    public TrainerRecord.TrainerResponseWithTrainees getTrainerByUserUsername_(String username) {
        Trainer trainer =getTrainerByUserUsername(username);
        return  trainerMapper.trainerToTrainerResponseWithTrainees(trainer);

    }

    public TrainerRecord.TrainerResponseWithTrainees updateTrainer_(TrainerRecord.TrainerUpdateRequest trainerRequest) {
        Trainer trainer_ = updateTrainer(trainerMapper.trainerUpdateRequestToTrainer(trainerRequest));
        TrainerRecord.TrainerResponseWithTrainees trainer = trainerMapper.trainerToTrainerResponseWithTrainees(trainer_);
        return trainer;
    }

    public List<TrainingRecord.TrainerTrainingResponse> getTrainerByUserUsernameWithTrainingParams_(TrainerRecord.TrainerRequestWithTrainingParams trainerRequest) {
        if (trainerRequest.trainer_username() == null || trainerRequest.trainer_username().isEmpty()) {
            return null;
        }
        Trainer trainer = getTrainerByUserUsernameWithTrainingParams(trainerRequest);
        TrainerRecord.TrainerResponseWithTrainings trainerResponse = trainerMapper.trainerToTrainerResponseWithTrainings(trainer);
        if (trainerResponse == null) {
            return null;
        }
        return trainerResponse.trainingList();
    }

    public ResponseEntity updateStatus(String username, boolean isActive) {
        Trainer trainer = getTrainerByUserUsername(username);
        if (trainer == null) {
            return ResponseEntity.notFound().build();
        }
        User user = trainer.getUser();
        user.setIsActive(isActive);
        trainer.setUser(user);
        if (updateTrainer(trainer) == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }
}
