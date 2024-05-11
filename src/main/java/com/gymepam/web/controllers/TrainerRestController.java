package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainerRecord.TrainerResponseWithTrainees;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TrainerFacadeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "Trainer Controller", value = "Operations for creating, updating, and retrieving Trainers information in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/trainer")
public class TrainerRestController {


    private TrainerFacadeService trainerFacade;

    @ApiOperation(value = "Save Trainer", notes = "Register a new Trainer in the system")
    @PostMapping("/public/save")
    public ResponseEntity<AuthenticationRequest> saveTrainer(@RequestBody @Validated TrainerRecord.TrainerRequest trainer){
        return trainerFacade.save_Trainer(trainer);
    }

    @ApiOperation(value = "Update Trainer", notes = "Update an existing Trainer")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PutMapping("/update")
    public ResponseEntity<TrainerResponseWithTrainees> updateTrainer(@RequestBody @Validated TrainerRecord.TrainerUpdateRequest trainerRequest){
        TrainerResponseWithTrainees trainerResponse = trainerFacade.updateTrainer_(trainerRequest);
        if (trainerResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(trainerResponse,HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Get Trainer", notes = "Retrieve an existing Trainer")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @GetMapping("/{username}")
    public ResponseEntity<TrainerRecord.TrainerResponseWithTrainees> getTrainer(@PathVariable String username){
        TrainerRecord.TrainerResponseWithTrainees trainerResponse = trainerFacade.getTrainerByUserUsername_(username);
        if (trainerResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(trainerResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Trainer Trainings List", notes = "Retrieve Trainer's Training List")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
//    @CircuitBreaker(name = "trainingCB", fallbackMethod = "fallGetTrainerByTrainingParams")
    @PostMapping("/trainings")
    public ResponseEntity<List<TrainingRecord.TrainerTrainingResponse>> getTrainerByTrainingParams(@RequestBody TrainingRecord.TrainerTrainingParamsRequest trainerRequest) {
        return trainerFacade.getTrainerByUserUsernameWithTrainingParams(trainerRequest);
    }

    @ApiOperation(value = "Update Trainer Status", notes = "Activate/De-Activate Trainer")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PatchMapping
    public ResponseEntity updateStatus(@RequestParam String username, @RequestParam boolean isActive){
        return trainerFacade.updateStatus(username, isActive);
    }

}
