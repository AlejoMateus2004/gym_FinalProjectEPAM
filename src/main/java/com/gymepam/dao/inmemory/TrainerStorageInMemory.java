package com.gymepam.dao.inmemory;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.Trainer;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class TrainerStorageInMemory implements TrainerRepo {

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

    @Override
    public Trainer findTrainerByUserUsername(String username) {
        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        return trainerList.stream()
                .filter(trainer -> trainer.getUser().getUserName().equals(username))
                .findFirst().orElse(null);
    }

    @Override
    public void deleteByUserUserName(String username) {
        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        Trainer trainer = trainerList.stream()
                .filter(t -> t.getUser().getUserName().equals(username))
                .findFirst().orElse(null);
        trainerMap.remove(trainer.getId());
    }

    @Override
    public List<Trainer> findTrainersByUserIsActiveAndTraineeListIsEmpty() {

        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        return trainerList.stream()
                .filter(trainer -> trainer.getUser().getIsActive().equals(true) && trainer.getTraineeList().isEmpty())
                .collect(Collectors.toList());

    }
}
