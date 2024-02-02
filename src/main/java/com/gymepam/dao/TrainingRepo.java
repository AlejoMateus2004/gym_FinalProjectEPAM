package com.gymepam.dao;

import com.gymepam.domain.entities.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingRepo {
    Training save(Training value);
    Optional<Training> findById(Long value);
    void delete(Training value);
    List<Training> findAll();
    List<Training> findTrainingByTrainee(String username);
    List<Training> findTrainingByTrainer(String username);


}
