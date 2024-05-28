package com.gymepam.service.training;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TrainingInMemoryStorage {

    private static Map<String, Object> trainingResponses = new HashMap<>();

    public Object getTrainingResponse(String processId) {
        var response = trainingResponses.getOrDefault(processId, null);
        trainingResponses.remove(processId);
        return response;
    }

    public void setTrainingResponse(String idResponse, Object trainingResponse) {
       trainingResponses.put(idResponse, trainingResponse);
    }
}
