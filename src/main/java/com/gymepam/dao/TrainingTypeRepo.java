package com.gymepam.dao;

import com.gymepam.domain.entities.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepo {
    TrainingType save(TrainingType value);
    Optional<TrainingType> findById(Long value);
    void delete(TrainingType value);
    List<TrainingType> findAll();


}
