package com.gymepam.web.controllers;

import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.TrainingTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Training Type Controller", description = "Operations for retrieving Training Types in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/training-type")
public class TrainingTypeRestController {

    TrainingTypeService trainingTypeService;

    @Operation(summary = "Get Training Type List", description = "Get All Training Types")
    @GetMapping("/all")
    public ResponseEntity<List<TrainingType>> getTrainingTypeList(){
        return new ResponseEntity<>(trainingTypeService.getAllTrainingTypes(),HttpStatus.CREATED);
    }


}
