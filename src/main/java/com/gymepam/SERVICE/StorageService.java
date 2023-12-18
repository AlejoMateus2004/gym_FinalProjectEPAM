package com.gymepam.SERVICE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingService trainingService;
    private Map<String, List<Object>> dbInMemory = new HashMap<>();

    @Autowired
    public StorageService(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Map<String, List<Object>> getDbInMemory() {
        return dbInMemory;
    }

    public void setDbInMemory(Map<String, List<Object>> dbInMemory) {
        this.dbInMemory = dbInMemory;
    }

    public void loadDbInMemory() {
        try {
            dbInMemory.put("Trainee", new ArrayList<>(traineeService.getAllTrainees()));
            dbInMemory.put("Trainer", new ArrayList<>(trainerService.getAllTrainers()));
            dbInMemory.put("Training", new ArrayList<>(trainingService.getAllTrainings()));
            LOGGER.info("Data loaded into dbInMemory");
        } catch (Exception e) {
            LOGGER.error("Error loading data into dbInMemory", e);
        }
    }
}
