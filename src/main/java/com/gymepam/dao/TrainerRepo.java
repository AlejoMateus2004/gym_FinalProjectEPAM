package com.gymepam.dao;

import com.gymepam.domain.entities.Trainer;

import java.time.LocalDate;
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

//    Trainer findTrainerByUserUsernameWithTrainingParams(String userName,
//                                                        LocalDate periodFrom,
//                                                        LocalDate periodTo,
//                                                        String traineeName);

    Set<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername);


}
