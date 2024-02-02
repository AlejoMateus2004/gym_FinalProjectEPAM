package com.gymepam.service;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.entities.Training;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
public class TrainingService {

    @Autowired
    private TrainingRepo trainingRepository;

    @Transactional
    public Training saveTraining(Training training) {
        try{
            return trainingRepository.save(training);
        } catch (Exception e) {
            log.error("Error, trying to save Training", e);
        }
        return null;
    }
    @Transactional(readOnly = true)
    public Training getTraining(Long trainingId) {
        return trainingRepository.findById(trainingId).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<Training> getAllTrainingsByTrainee(String username) {
        return trainingRepository.findTrainingByTrainee(username);
    }
    @Transactional(readOnly = true)
    public List<Training> getAllTrainingsByTrainer(String username) {
        return trainingRepository.findTrainingByTrainer(username);
    }



}
