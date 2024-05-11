package com.gymepam.web.controllers;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TrainingFacadeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Training Controller", value = "Operations for creating Trainings in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/training")
public class TrainingRestController {

    private TrainingFacadeService trainingFacadeService;

    @ApiOperation(value = "Save Training", notes = "Register a new Training in the system")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @CircuitBreaker(name = "trainingCB", fallbackMethod = "fallBackSaveTraining")
    @PostMapping
    public ResponseEntity saveTraining(@RequestBody @Validated TrainingRecord.TrainingRequest trainingRequest){
        return trainingFacadeService.saveTraining(trainingRequest);
    }

    @ApiOperation(value = "Update Training Status", notes = "Update Training status to completed")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @CircuitBreaker(name = "trainingCB", fallbackMethod = "fallBackSaveTraining")
    @PutMapping("/status/{trainingId}")
    public ResponseEntity updateTrainingStatus(@PathVariable Long trainingId){
        return trainingFacadeService.updateTrainingStatus(trainingId);
    }

    @ApiOperation(value = "Get Training List by Trainer username", notes = "Retrieve Training List by Trainer username")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @CircuitBreaker(name = "trainingCB", fallbackMethod = "fallBackGetTrainingSummary")
    @GetMapping("/summary/trainer/{trainerUsername}")
    public ResponseEntity<TrainingRecord.TrainerDetailsTrainingSummary> getTrainingSummaryByTrainerUsername(@PathVariable String trainerUsername){
        return trainingFacadeService.getTrainingSummaryByTrainer(trainerUsername);
    }

    @ApiOperation(value = "Delete Trainings By Trainer Username")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @DeleteMapping("/{trainingId}")
    public ResponseEntity<String> deleteTrainingByTrainerUsername(@PathVariable Long trainingId){
        return trainingFacadeService.deleteTrainingById(trainingId);
    }

    public ResponseEntity fallBackSaveTraining(@RequestBody @Validated TrainingRecord.TrainingRequest trainingRequest, RuntimeException e){
        return new ResponseEntity<>("Training service is not working", HttpStatus.OK);
    }

    public ResponseEntity<TrainingRecord.TrainerDetailsTrainingSummary> fallBackGetTrainingSummary(@PathVariable String trainerUsername, RuntimeException e){
        return new ResponseEntity("Training service is not working", HttpStatus.OK);

    }


}
