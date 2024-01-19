package com.gymepam.dao.inmemory;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.Trainee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
public class TraineeStorageInMemory implements TraineeRepo {

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
        traineeMap.remove(trainee.getId());
    }
}
