package com.gymepam.dao;

import com.gymepam.domain.entities.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TrainerRepo {
    Trainer save(Trainer value);
    Optional<Trainer> findById(Long value);
    void delete(Trainer value);
    List<Trainer> findAll();
    Trainer findTrainerByUserUsername(String username);
    void deleteByUserUserName(String username);
    List<Trainer> findTrainersByUserIsActiveAndTraineeListIsEmpty();
    Set<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername);


}
