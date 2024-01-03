package com.gymepam.SERVICE;

import com.gymepam.DAO.Training_TypeRepo;
import com.gymepam.DOMAIN.Training_Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrainingTypeService {

    private static Training_TypeRepo Training_TypeRepository;

    @Autowired
    public TrainingTypeService(@Qualifier("InMemoryTraining_Type") Training_TypeRepo Training_TypeRepository) {
        this.Training_TypeRepository = Training_TypeRepository;
    }

    public void setTraining_TypeRepository(Training_TypeRepo Training_TypeRepository) {
        this.Training_TypeRepository = Training_TypeRepository;
    }

    public Training_Type saveTraining_Type(Training_Type Training_Type) {
        return Training_TypeRepository.save(Training_Type);
    }

    public Training_Type getTraining_Type(Long Training_TypeId) {
        return Training_TypeRepository.findById(Training_TypeId).orElse(null);
    }

    public List<Training_Type> getAllTraining_Types() {
        return Training_TypeRepository.findAll();
    }



}
