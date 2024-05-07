package com.gymepam.dao.inmemory;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.entities.Trainer;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TrainerStorageInMemory implements TrainerRepo {

    private static Map<Long, Trainer> trainerMap = new HashMap<>();

    @Override
    public Trainer save(Trainer value) {
        if (value == null) {
            return null;
        }
        trainerMap.put(value.getTrainerId(), value);
        return trainerMap.get(value.getTrainerId());
    }

    @Override
    public Optional<Trainer> findById(Long value) {
        return Optional.ofNullable(trainerMap.get(value));
    }

    @Override
    public void delete(Trainer value) {
        trainerMap.remove(value.getTrainerId());
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
        trainerMap.remove(trainer.getTrainerId());
    }

    @Override
    public List<Trainer> findTrainersByUserIsActiveAndTraineeListIsEmpty() {

        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        return trainerList.stream()
                .filter(trainer -> trainer.getUser().getIsActive().equals(true) && trainer.getTraineeList().isEmpty())
                .collect(Collectors.toList());

    }

    @Override
    public Trainer findTrainerByUserUsernameWithTrainingParams(String userName, LocalDate periodFrom, LocalDate periodTo, String traineeName) {
        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());
        Trainer trainer = trainerList.stream()
                .filter(t -> t.getUser().getUserName().equals(userName) &&
                        t.getTrainingList().stream()
                                .anyMatch(tr -> (periodFrom == null || tr.getTrainingDate().isAfter(periodFrom)) &&
                                        (periodTo == null || tr.getTrainingDate().isBefore(periodTo)) &&
                                        (traineeName == null || tr.getTrainee().getUser().getFirstName().toLowerCase().contains(traineeName.toLowerCase())))
                )
                .findFirst()
                .orElse(null);

        return trainer;
    }

    @Override
    public Set<Trainer> findActiveTrainersNotAssignedToTrainee(String traineeUsername) {
        List<Trainer> trainerList = new ArrayList<>(trainerMap.values());

        Set<Trainer> trainersNotAssignedToTrainee = trainerList.stream()
                .filter(trainer -> trainer.getUser().getIsActive() && !isTrainerAssignedToTrainee(trainer, traineeUsername))
                .collect(Collectors.toSet());

        return trainersNotAssignedToTrainee;
    }

    private boolean isTrainerAssignedToTrainee(Trainer trainer, String traineeUsername) {
        return trainer.getTraineeList().stream()
                .anyMatch(trainee -> trainee.getUser().getUserName().equals(traineeUsername));
    }
}
