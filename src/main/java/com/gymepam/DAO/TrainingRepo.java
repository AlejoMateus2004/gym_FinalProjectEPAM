package com.gymepam.DAO;

import com.gymepam.DOMAIN.Training;

import java.util.List;
import java.util.Optional;

public interface TrainingRepo {
    Training save(Training value);
    Optional<Training> findById(Long value);
    void delete(Training value);
    List<Training> findAll();


}
