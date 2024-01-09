package com.gymepam.service;

import com.gymepam.dao.Trainee2TrainerRepo;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class Trainee2TrainerService {

    @Autowired
    Trainee2TrainerRepo trainee2TrainerRepo;

    @Transactional
    public Trainee2Trainer save(Trainee2Trainer trainee2Trainer){
        return trainee2TrainerRepo.save(trainee2Trainer);
    }

    @Transactional(readOnly = true)
    public List<Trainee2Trainer> getTrainerListByTrainee(Trainee trainee){
        return trainee2TrainerRepo.findTrainee2TrainerByTrainee(trainee);
    }


}
