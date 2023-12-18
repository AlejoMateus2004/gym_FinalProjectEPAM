package com.gymepam.SERVICE;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TraineeService {

    private static Repo<Trainee> traineeRepository;

    @Autowired
    public TraineeService(@Qualifier("InMemoryTrainee") Repo<Trainee> traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public void setTraineeRepository(Repo<Trainee> traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public Trainee saveTrainee(Trainee trainee) {
        return traineeRepository.save(trainee);
    }


    public Trainee getTrainee(Long traineeId) {
        return traineeRepository.findById(traineeId).orElse(null);
    }


    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }
}
