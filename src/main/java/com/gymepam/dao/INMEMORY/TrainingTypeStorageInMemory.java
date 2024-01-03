package com.gymepam.dao.INMEMORY;

import com.gymepam.dao.TrainingTypeRepo;
import com.gymepam.domain.TrainingType;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainingTypeStorageInMemory implements TrainingTypeRepo {

    private static Map<Long, TrainingType> Training_TypeMap = new HashMap<>();

    @Override
    public TrainingType save(TrainingType value) {
        if (value == null) {
            return null;
        }
        Training_TypeMap.put(value.getId(), value);
        return Training_TypeMap.get(value.getId());
    }

    @Override
    public Optional<TrainingType> findById(Long value) {
        return Optional.ofNullable(Training_TypeMap.get(value));
    }

    @Override
    public void delete(TrainingType value) {
        Training_TypeMap.remove(value.getId());
    }

    @Override
    public List<TrainingType> findAll() {
        return new ArrayList<>(Training_TypeMap.values());
    }
}
