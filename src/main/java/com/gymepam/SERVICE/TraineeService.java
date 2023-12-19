package com.gymepam.SERVICE;

import com.gymepam.DAO.TraineeRepo;
import com.gymepam.DOMAIN.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TraineeService {

    private static TraineeRepo traineeRepository;

    @Autowired
    public TraineeService(@Qualifier("InMemoryTrainee") TraineeRepo traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public void setTraineeRepository(TraineeRepo traineeRepository) {
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

    public void deleteTrainee(Trainee trainee) {
        traineeRepository.delete(trainee);
    }
}
