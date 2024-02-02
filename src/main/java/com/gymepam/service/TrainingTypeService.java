package com.gymepam.service;

import com.gymepam.dao.TrainingTypeRepo;
import com.gymepam.domain.entities.TrainingType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
public class TrainingTypeService {

    @Autowired
    private TrainingTypeRepo trainingTypeRepository;

    @Transactional
    public TrainingType saveTraining_Type(TrainingType TrainingType) {
        try{
            return trainingTypeRepository.save(TrainingType);

        } catch (Exception e) {
            log.error("Error, trying to save Training Type", e);

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
