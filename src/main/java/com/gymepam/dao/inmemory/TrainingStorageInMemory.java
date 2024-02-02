package com.gymepam.dao.inMemory;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.entities.Training;

import java.util.*;
import java.util.stream.Collectors;

public class TrainingStorageInMemory implements TrainingRepo {

    private static Map<Long, Training> trainingMap = new HashMap<>();

    @Override
    public Training save(Training value) {
        if (value == null) {
            return null;
        }
        trainingMap.put(value.getId(), value);
        return trainingMap.get(value.getId());
    }

    @Override
    public Optional<Training> findById(Long value) {
        return Optional.ofNullable(trainingMap.get(value));
    }

    @Override
    public void delete(Training value) {
        trainingMap.remove(value.getId());
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    @Override
    public List<Training> findTrainingByTrainee(String username) {
        return trainingMap.values().stream()
                .filter(t -> t.getTrainee().getUser().getUserName().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public List<Training> findTrainingByTrainer(String username) {
        return trainingMap.values().stream()
                .filter(t -> t.getTrainer().getUser().getUserName().equals(username))
                .collect(Collectors.toList());
    }

}
