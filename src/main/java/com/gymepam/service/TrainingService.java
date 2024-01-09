package com.gymepam.service;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    private TrainingRepo trainingRepository;

    public void setTrainingRepository(TrainingRepo trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    @Transactional
    public Training saveTraining(Training training) {
        try{
            return trainingRepository.save(training);
        } catch (Exception e) {
            logger.error("Error, trying to save Training", e);

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
