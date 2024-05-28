package com.gymepam.web.controllers;

import com.gymepam.service.training.TrainingInMemoryStorage;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Rolling Controller", description = "Operations for getting response")
@RestController
@RequestMapping("/response")
public class RollingRestController {

    @Autowired
    private TrainingInMemoryStorage trainingInMemoryStorage;

    @GetMapping("/{processId}")
    public ResponseEntity<Object>  getResponse(@PathVariable String processId){
        var response = trainingInMemoryStorage.getTrainingResponse(processId);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
