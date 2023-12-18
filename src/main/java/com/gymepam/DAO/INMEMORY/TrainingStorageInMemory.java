package com.gymepam.DAO.INMEMORY;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Training;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("InMemoryTraining")
public class TrainingStorageInMemory implements Repo<Training> {

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
}
