package com.gymepam.service.facade;

import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;
import com.gymepam.domain.Trainer;
import com.gymepam.service.Trainee2TrainerService;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class Trainee2TrainerFacadeService {

    Trainee2TrainerService trainee2TrainerService;
    TrainerService trainerService;
    TraineeService traineeService;

    @Autowired
    public Trainee2TrainerFacadeService(Trainee2TrainerService trainee2TrainerService, TrainerService trainerService, TraineeService traineeService) {
        this.trainee2TrainerService = trainee2TrainerService;
        this.trainerService = trainerService;
        this.traineeService = traineeService;
    }

    public Trainee2Trainer save(Map<String,String> trainee2TrainerMap){
        String traineeUsername = trainee2TrainerMap.get("Trainee");
        String trainerUsername = trainee2TrainerMap.get("Trainer");

        Trainer trainer = trainerService.getTrainerByUserUsername(trainerUsername);
        Trainee trainee = traineeService.getTraineeByUserUsername(traineeUsername);

        Trainee2Trainer trainee2Trainer = new Trainee2Trainer();
        trainee2Trainer.setTrainee(trainee);
        trainee2Trainer.setTrainer(trainer);
        return trainee2TrainerService.save(trainee2Trainer);
    }

    public List<Trainee2Trainer> getTrainerListByTrainee(String username){
        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        return trainee2TrainerService.getTrainerListByTrainee(trainee);
    }

}
