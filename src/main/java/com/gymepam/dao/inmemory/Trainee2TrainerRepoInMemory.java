package com.gymepam.dao.inmemory;

import com.gymepam.dao.Trainee2TrainerRepo;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
@Log4j2
public class Trainee2TrainerRepoInMemory implements Trainee2TrainerRepo {

    @Override
    public Trainee2Trainer save(Trainee2Trainer trainee2Trainer) {
        log.error("Trainee2Trainer save not implemented");
        return null;
    }

    @Override
    public List<Trainee2Trainer> findTrainee2TrainerByTrainee(Trainee trainee) {
        log.error("List<Trainee2Trainer> findTrainee2TrainerByTrainee not implemented");
        return null;
    }
}
