package com.gymepam.DAO.INMEMORY;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Trainee;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("InMemoryTrainee")
public class TraineeStorageInMemory implements Repo<Trainee> {

    private static Map<Long, Trainee> traineeMap = new HashMap<>();

    @Override
    public Trainee save(Trainee value) {
        if (value == null) {
            return null;
        }
        traineeMap.put(value.getId(), value);
        return traineeMap.get(value.getId());
    }

    @Override
    public Optional<Trainee> findById(Long value) {
        return Optional.ofNullable(traineeMap.get(value));
    }

    @Override
    public void delete(Trainee value) {
        traineeMap.remove(value.getId());
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }
}
