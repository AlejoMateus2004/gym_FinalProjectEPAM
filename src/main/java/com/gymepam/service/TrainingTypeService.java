package com.gymepam.service;

import com.gymepam.dao.TrainingTypeRepo;
import com.gymepam.domain.TrainingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainingTypeService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingTypeService.class);

    @Autowired
    private TrainingTypeRepo trainingTypeRepository;

    public void setTrainingTypeRepository(TrainingTypeRepo trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }
    @Transactional
    public TrainingType saveTraining_Type(TrainingType TrainingType) {
        try{
            return trainingTypeRepository.save(TrainingType);

        } catch (Exception e) {
            logger.error("Error, trying to save Training Type", e);

        }
        return null;
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
