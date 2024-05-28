package com.gymepam.web.controllers;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.training.facade.TrainingFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Training Controller", description = "Operations for creating Trainings in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/training")
public class TrainingRestController {

    private TrainingFacadeService trainingFacadeService;

    @Operation(summary = "Save Training", description = "Register a new Training in the system")
    @PostMapping
    public ResponseEntity<GlobalModelResponse> saveTraining(@RequestBody @Validated TrainingRecord.TrainingRequest trainingRequest){
        return trainingFacadeService.saveTraining(trainingRequest);
    }

    @Operation(summary = "Update Training Status", description = "Update Training status to completed")
    @PutMapping("/status/{trainingId}")
    public ResponseEntity<GlobalModelResponse> updateTrainingStatus(@PathVariable Long trainingId){
        return trainingFacadeService.updateTrainingStatus(trainingId);
    }

    @Operation(summary = "Get Training List by Trainer username", description = "Retrieve Training List by Trainer username")
    @GetMapping("/summary/trainer/{trainerUsername}")
    public ResponseEntity<GlobalModelResponse> getTrainingSummaryByTrainerUsername(@PathVariable String trainerUsername){
        return trainingFacadeService.getTrainingSummaryByTrainer(trainerUsername);
    }

    @Operation(summary = "Delete Trainings By Trainer Username")
    @DeleteMapping("/{trainingId}")
    public ResponseEntity<GlobalModelResponse> deleteTrainingByTrainerUsername(@PathVariable Long trainingId){
        return trainingFacadeService.deleteTrainingById(trainingId);
    }

}
