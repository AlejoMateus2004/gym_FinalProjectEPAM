package com.gymepam.web.controllers;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeRequest;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeResponseWithTrainers;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.trainee.facade.TraineeFacadeService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Tag(name = "Trainee Controller", description = "Operations for creating, updating, retrieving and deleting Trainees in the application")
@RestController
@RequestMapping("/trainee")
public class TraineeRestController {

    private final TraineeFacadeService traineeFacade;
    private final Counter trainee_registration_total;

    public TraineeRestController(TraineeFacadeService traineeFacade, MeterRegistry meterRegistry) {
        this.traineeFacade = traineeFacade;
        this.trainee_registration_total = Counter.builder("trainee_registration_total")
                .description("Total of successful trainee registration").register(meterRegistry);
    }

    @Operation(summary = "Save Trainee", description = "Register a new Trainee in the system")
    @PostMapping("/public/save")
    public ResponseEntity<AuthenticationRequest> saveTrainee(@RequestBody @Validated TraineeRequest trainee){
       var response = traineeFacade.save_Trainee(trainee);
        if (response.getBody() != null) trainee_registration_total.increment();
        return response;
    }

    @Operation(summary = "Update Trainee", description = "Update an existing Trainee")
    @PutMapping("/update")
    public ResponseEntity<TraineeResponseWithTrainers> updateTrainee(@RequestBody @Validated TraineeRecord.TraineeUpdateRequest traineeRequest){
        TraineeResponseWithTrainers traineeResponse = traineeFacade.updateTrainee_(traineeRequest);
        if (traineeResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(traineeResponse,HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Get Trainee", description = "Retrieve an existing Trainee")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseWithTrainers> getTrainee(@PathVariable String username){
        TraineeResponseWithTrainers traineeResponse = traineeFacade.getTraineeByUserUsername_(username);
        if (traineeResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(traineeResponse, HttpStatus.OK);
    }

    @Operation(summary = "Update Trainee's Trainer List", description = "Retrieve Trainee's Trainer List")
    @PutMapping("/trainers")
    public ResponseEntity<Set<TrainerRecord.TrainerResponse>> updateTraineesTrainerList(@RequestBody @Validated TraineeRecord.TraineeTrainerList trainee){
        try {
            Set<TrainerRecord.TrainerResponse> trainers = traineeFacade.updateTraineesTrainerList(trainee.trainee_username(), trainee.trainerUsernames());
            return new ResponseEntity<>(trainers, HttpStatus.CREATED);
        }catch(Exception e){
            log.error("Error updating Trainee's Trainer List",e);
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get Trainee Trainings List", description = "Retrieve Trainee's Training List")
    @PostMapping("/trainings")
    public ResponseEntity<GlobalModelResponse> getTraineeByTrainingParams(@RequestBody TrainingRecord.TraineeTrainingParamsRequest traineeRequest) {
        return traineeFacade.getTraineeByUserUsernameWithTrainingParams(traineeRequest);
    }

    @Operation(summary = "Delete Trainee")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable String username){
        try {
            traineeFacade.deleteTraineeByUserName(username);
            return new ResponseEntity<>("Deleted user", HttpStatus.OK);
        }catch(Exception e){
            log.error("Error deleting Trainee",e);
            return new ResponseEntity<>("User not deleted", HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Update Trainee Status", description = "Activate/De-Activate Trainee")
    @PatchMapping
    public ResponseEntity updateStatus(@RequestParam String username, @RequestParam boolean isActive){
        return traineeFacade.updateStatus(username, isActive);
    }

    @Operation(summary = "Get Not Assigned Trainers On Trainee", description = "Get Not Assigned Trainers On Active Trainee")
    @GetMapping("/{username}/trainers-notAssigned")
    public ResponseEntity<Set<TrainerRecord.TrainerResponse>> getNotAssignedTrainersOnTrainee(@PathVariable String username){
        Set<TrainerRecord.TrainerResponse> trainersNotAssigned = traineeFacade.getNotAssignedTrainersByTraineeUserUsername(username);
        if (trainersNotAssigned == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(trainersNotAssigned, HttpStatus.OK);
    }
}
