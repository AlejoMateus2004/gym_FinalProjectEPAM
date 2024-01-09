package com.gymepam.dao;

import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;

import java.util.List;

public interface Trainee2TrainerRepo {

    Trainee2Trainer save(Trainee2Trainer trainee2Trainer);

    List<Trainee2Trainer> findTrainee2TrainerByTrainee(Trainee trainee);
}
