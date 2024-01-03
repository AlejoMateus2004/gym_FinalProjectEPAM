package com.gymepam.service;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.Training;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class TrainingService {
    @Autowired
    private TrainingRepo trainingRepository;

    public void setTrainingRepository(TrainingRepo trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    @Transactional
    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
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
