package com.gymepam.DAO.INMEMORY;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Trainer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("InMemoryTrainer")
public class TrainerStorageInMemory implements Repo<Trainer> {

    private static Map<Long, Trainer> trainerMap = new HashMap<>();

    @Override
    public Trainer save(Trainer value) {
        if (value == null) {
            return null;
        }
        trainerMap.put(value.getId(), value);
        return trainerMap.get(value.getId());
    }

    @Override
    public Optional<Trainer> findById(Long value) {
        return Optional.ofNullable(trainerMap.get(value));
    }

    @Override
    public void delete(Trainer value) {
        trainerMap.remove(value.getId());
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }
}
