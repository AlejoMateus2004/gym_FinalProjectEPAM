package com.gymepam.web.controllers;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainerRecord.TrainerResponseWithTrainees;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.trainer.facade.TrainerFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trainer Controller", description = "Operations for creating, updating, and retrieving Trainers information in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/trainer")
public class TrainerRestController {


    private TrainerFacadeService trainerFacade;

    @Operation(summary = "Save Trainer", description = "Register a new Trainer in the system")
    @PostMapping("/public/save")
    public ResponseEntity<AuthenticationRequest> saveTrainer(@RequestBody @Validated TrainerRecord.TrainerRequest trainer){
        return trainerFacade.save_Trainer(trainer);
    }

    @Operation(summary = "Update Trainer", description = "Update an existing Trainer")
    @PutMapping("/update")
    public ResponseEntity<TrainerResponseWithTrainees> updateTrainer(@RequestBody @Validated TrainerRecord.TrainerUpdateRequest trainerRequest){
        TrainerResponseWithTrainees trainerResponse = trainerFacade.updateTrainer_(trainerRequest);
        if (trainerResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(trainerResponse,HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get Trainer", description = "Retrieve an existing Trainer")
    @GetMapping("/{username}")
    public ResponseEntity<TrainerRecord.TrainerResponseWithTrainees> getTrainer(@PathVariable String username){
        TrainerRecord.TrainerResponseWithTrainees trainerResponse = trainerFacade.getTrainerByUserUsername_(username);
        if (trainerResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(trainerResponse, HttpStatus.OK);
    }

    @Operation(summary = "Get Trainer Trainings List", description = "Retrieve Trainer's Training List")
    @PostMapping("/trainings")
    public ResponseEntity<GlobalModelResponse> getTrainerByTrainingParams(@RequestBody TrainingRecord.TrainerTrainingParamsRequest trainerRequest) {
        return trainerFacade.getTrainerByUserUsernameWithTrainingParams(trainerRequest);
    }

    @Operation(summary = "Update Trainer Status", description = "Activate/De-Activate Trainer")
    @PatchMapping
    public ResponseEntity updateStatus(@RequestParam String username, @RequestParam boolean isActive){
        return trainerFacade.updateStatus(username, isActive);
    }

}
