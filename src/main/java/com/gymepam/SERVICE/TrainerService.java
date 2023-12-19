package com.gymepam.SERVICE;

import com.gymepam.DAO.TrainerRepo;
import com.gymepam.DOMAIN.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerService {

    private static TrainerRepo trainerRepository;

    @Autowired
    public TrainerService(@Qualifier("InMemoryTrainer") TrainerRepo trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public void setTrainerRepository(TrainerRepo trainerRepository) {
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
