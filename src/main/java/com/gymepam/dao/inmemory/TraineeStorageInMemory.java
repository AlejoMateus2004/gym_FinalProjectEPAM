package com.gymepam.dao.inmemory;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.entities.Trainee;

import java.time.LocalDate;
import java.util.*;


public class TraineeStorageInMemory implements TraineeRepo {

    private static Map<Long, Trainee> traineeMap = new HashMap<>();

    @Override
    public Trainee save(Trainee value) {
        if (value == null) {
            return null;
        }
        traineeMap.put(value.getTraineeId(), value);
        return traineeMap.get(value.getTraineeId()); }

    @Override
    public Optional<Trainee> findById(Long value) {
        return Optional.ofNullable(traineeMap.get(value));
    }

    @Override
    public void delete(Trainee value) {
        traineeMap.remove(value.getTraineeId());
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }

    @Override
    public Trainee findTraineeByUserUsername(String username) {
        List<Trainee> traineeList = new ArrayList<>(traineeMap.values());
        return traineeList.stream()
                .filter(trainee -> trainee.getUser().getUserName().equals(username))
                .findFirst().orElse(null);
    }

    @Override
    public void deleteByUserUserName(String username) {
        List<Trainee> traineeList = new ArrayList<>(traineeMap.values());
        Trainee trainee = traineeList.stream()
                .filter(t -> t.getUser().getUserName().equals(username))
                .findFirst().orElse(null);
        traineeMap.remove(trainee.getTraineeId());
    }



//    @Override
//    public Trainee findTraineeByUserUsernameWithTrainingParams(String userName, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType) {
//        List<Trainee> traineeList = new ArrayList<>(traineeMap.values());
//        Trainee trainee = traineeList.stream()
//                .filter(t -> t.getUser().getUserName().equals(userName) &&
//                        t.getTrainingList().stream()
//                                .anyMatch(tr -> (periodFrom == null || tr.getTrainingDate().isAfter(periodFrom)) &&
//                                        (periodTo == null || tr.getTrainingDate().isBefore(periodTo)) &&
//                                        (trainerName == null || tr.getTrainer().getUser().getFirstName().toLowerCase().contains(trainerName.toLowerCase())) &&
//                                        (trainingType == null || tr.getTrainingType().getTrainingTypeName().toLowerCase().contains(trainingType.toLowerCase())))
//                )
//                .findFirst()
//                .orElse(null);
//
//        return trainee;
//    }


}
