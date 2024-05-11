package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeRequest;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeResponseWithTrainers;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TraineeFacadeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Api(tags = "Trainee Controller", value = "Operations for creating, updating, retrieving and deleting Trainees in the application")
@RestController
@RequestMapping("/trainee")
public class TraineeRestController {

    TraineeFacadeService traineeFacade;

    Counter trainee_registration_total;

    public TraineeRestController(TraineeFacadeService traineeFacade, MeterRegistry meterRegistry) {
        this.traineeFacade = traineeFacade;
        this.trainee_registration_total = Counter.builder("trainee_registration_total")
                .description("Total of successful trainee registration").register(meterRegistry);
    }

    @ApiOperation(value = "Save Trainee", notes = "Register a new Trainee in the system")
    @PostMapping("/public/save")
    public ResponseEntity<AuthenticationRequest> saveTrainee(@RequestBody @Validated TraineeRequest trainee){
       var response = traineeFacade.save_Trainee(trainee);
        if (response.getBody() != null) trainee_registration_total.increment();
        return response;
    }

    @ApiOperation(value = "Update Trainee", notes = "Update an existing Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PutMapping("/update")
    public ResponseEntity<TraineeResponseWithTrainers> updateTrainee(@RequestBody @Validated TraineeRecord.TraineeUpdateRequest traineeRequest){
        TraineeResponseWithTrainers traineeResponse = traineeFacade.updateTrainee_(traineeRequest);
        if (traineeResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(traineeResponse,HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Get Trainee", notes = "Retrieve an existing Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @GetMapping("/{username}")
    public ResponseEntity<TraineeResponseWithTrainers> getTrainee(@PathVariable String username){
        TraineeResponseWithTrainers traineeResponse = traineeFacade.getTraineeByUserUsername_(username);
        if (traineeResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return new ResponseEntity<>(traineeResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Update Trainee's Trainer List", notes = "Retrieve Trainee's Trainer List")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
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

    @ApiOperation(value = "Get Trainee Trainings List", notes = "Retrieve Trainee's Training List")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
//    @CircuitBreaker(name = "trainingCB", fallbackMethod = "fallGetTraineeByTrainingParams")
    @PostMapping("/trainings")
    public ResponseEntity<List<TrainingRecord.TraineeTrainingResponse>> getTraineeByTrainingParams(@RequestBody TrainingRecord.TraineeTrainingParamsRequest traineeRequest) {
        return traineeFacade.getTraineeByUserUsernameWithTrainingParams(traineeRequest);
    }

    @ApiOperation(value = "Delete Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
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

    @ApiOperation(value = "Update Trainee Status", notes = "Activate/De-Activate Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PatchMapping
    public ResponseEntity updateStatus(@RequestParam String username, @RequestParam boolean isActive){
        return traineeFacade.updateStatus(username, isActive);
    }

    @ApiOperation(value = "Get Not Assigned Trainers On Trainee", notes = "Get Not Assigned Trainers On Active Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @GetMapping("/{username}/trainers-notAssigned")
    public ResponseEntity<Set<TrainerRecord.TrainerResponse>> getNotAssignedTrainersOnTrainee(@PathVariable String username){
        Set<TrainerRecord.TrainerResponse> trainersNotAssigned = traineeFacade.getNotAssignedTrainersByTraineeUserUsername(username);
        if (trainersNotAssigned == null) {
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(trainersNotAssigned, HttpStatus.OK);
    }
}
