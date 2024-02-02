package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeRequest;
import com.gymepam.domain.dto.records.TraineeRecord.TraineeResponseWithTrainers;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TraineeFacadeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Api(tags = "Trainee Controller", value = "Operations for creating, updating, retrieving and deleting Trainees in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/trainee")
public class TraineeRestController {

    TraineeFacadeService traineeFacade;

    @ApiOperation(value = "Save Trainee", notes = "Register a new Trainee in the system")
    @PostMapping("/save")
    public ResponseEntity<AuthenticationRequest> saveTrainee(@RequestBody @Valid TraineeRequest trainee){
        return traineeFacade.save_Trainee(trainee);
    }
    @ApiOperation(value = "Update Trainee", notes = "Update an existing Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PutMapping("/update")
    public ResponseEntity<TraineeResponseWithTrainers> updateTrainee(@RequestBody @Valid TraineeRecord.TraineeUpdateRequest traineeRequest){
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
    public ResponseEntity<Set<TrainerRecord.TrainerResponse>> updateTraineesTrainerList(@RequestBody @Valid TraineeRecord.TraineeTrainerList trainee){
        try {
            Set<TrainerRecord.TrainerResponse> trainers = traineeFacade.updateTraineesTrainerList(trainee.trainee_username(), trainee.trainerUsernames());
            return new ResponseEntity<>(trainers, HttpStatus.CREATED);
        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.notFound().build();
        }
    }
    @ApiOperation(value = "Get Trainee Trainings List", notes = "Retrieve Trainee's Training List")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @GetMapping("/trainings")
    public ResponseEntity<List<TrainingRecord.TraineeTrainingResponse>> getTraineeTrainingsList(
            @RequestParam(name = "username", required = true) String username,
            @RequestParam(name = "periodFrom", required = false) LocalDate periodFrom,
            @RequestParam(name = "periodTo", required = false) LocalDate periodTo,
            @RequestParam(name = "trainerName", required = false) String trainerName,
            @RequestParam(name = "trainingType", required = false) String trainingType) {

        TraineeRecord.TraineeRequestWithTrainingParams traineeRequest =
                new TraineeRecord.TraineeRequestWithTrainingParams(username, new TrainingRecord.TrainingFilterRequest(periodFrom, periodTo, trainerName, trainingType));
        List<TrainingRecord.TraineeTrainingResponse> trainingsResponse = traineeFacade.getTraineeByUserUsernameWithTrainingParams_(traineeRequest);
        if (trainingsResponse == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>(trainingsResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete Trainee")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable String username){
        try {
            traineeFacade.deleteByUserUserName(username);
            return new ResponseEntity<>("Deleted user", HttpStatus.OK);
        }catch(Exception e){
            System.out.println(e);
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
