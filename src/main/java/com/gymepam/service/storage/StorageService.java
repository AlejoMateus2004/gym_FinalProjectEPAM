package com.gymepam.service.storage;

import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainer.TrainerService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Data
@Service
public class StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private TraineeService traineeService;
    private TrainerService trainerService;
    private Map<String, List<Object>> dbInMemory = new HashMap<>();

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
            LOGGER.info("Data loaded into dbInMemory");
        } catch (Exception e) {
            LOGGER.error("Error loading data into dbInMemory", e);
        }
    }
}
