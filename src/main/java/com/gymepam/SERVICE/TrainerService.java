package com.gymepam.SERVICE;

import com.gymepam.DAO.Repo;
import com.gymepam.DAO.TrainerRepository;
import com.gymepam.DOMAIN.Trainee;
import com.gymepam.DOMAIN.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private static Repo<Trainer> trainerRepository;

    @Autowired
    public TrainerService(@Qualifier("InMemoryTrainer") Repo<Trainer> trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void setTrainerRepository(Repo<Trainer> trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Trainer saveTrainer(Trainer trainer) {
        return trainerRepository.save(trainer);
    }

    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId).orElse(null);
    }

    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

}
