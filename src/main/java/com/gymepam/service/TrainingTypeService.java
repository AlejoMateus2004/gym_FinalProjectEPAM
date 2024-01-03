package com.gymepam.service;

import com.gymepam.dao.TrainingTypeRepo;
import com.gymepam.domain.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingTypeService {

    @Autowired
    private TrainingTypeRepo trainingTypeRepository;

    public void setTrainingTypeRepository(TrainingTypeRepo trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }
    @Transactional
    public TrainingType saveTraining_Type(TrainingType TrainingType) {
        return trainingTypeRepository.save(TrainingType);
    }
    @Transactional(readOnly = true)
    public TrainingType getTraining_Type(Long Training_TypeId) {
        return trainingTypeRepository.findById(Training_TypeId).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeRepository.findAll();
    }



}
