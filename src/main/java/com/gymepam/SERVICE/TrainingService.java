package com.gymepam.SERVICE;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Trainee;
import com.gymepam.DOMAIN.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrainingService {
    private static Repo<Training> trainingRepository;

    @Autowired
    public TrainingService(@Qualifier("InMemoryTraining") Repo<Training> trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public void setTrainingRepository(Repo<Training> trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }

    public Training getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId).orElse(null);
    }
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }


}
