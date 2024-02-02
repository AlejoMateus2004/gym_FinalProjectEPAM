package com.gymepam.dao;

import com.gymepam.domain.entities.Trainee;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeRepo {
    Trainee save(Trainee value);
    Optional<Trainee> findById(Long value);
    void delete(Trainee value);
    List<Trainee> findAll();
    Trainee findTraineeByUserUsername(String username);
    void deleteByUserUserName(String username);
    Trainee findTraineeByUserUsernameWithTrainingParams(String userName,
                                                        LocalDate periodFrom,
                                                        LocalDate periodTo,
                                                        String trainerName,
                                                        String trainingType);



}
