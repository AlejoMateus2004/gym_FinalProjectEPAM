package com.gymepam.web.controllers;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Trainee;
import com.gymepam.service.TraineeService;
import com.gymepam.service.facade.TraineeFacadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trainee")
public class TraineeRestController {

    private static final Logger logger = LoggerFactory.getLogger(TraineeRestController.class);

    TraineeFacadeService traineeFacade;

    TraineeService traineeService;

    @Autowired
    public TraineeRestController(TraineeFacadeService traineeFacade, TraineeService traineeService) {
        this.traineeFacade = traineeFacade;
        this.traineeService = traineeService;
    }

    @PostMapping("/save")
    public ResponseEntity<AuthenticationRequest> saveTrainee(@RequestBody @Valid Trainee trainee, Errors errors){
        if (errors.hasErrors()) {
            logger.error("Validation Error " + errors.getFieldError().getDefaultMessage());

            return ResponseEntity.badRequest().body(null);
        }
        AuthenticationRequest newAuth = traineeFacade.saveTrainee(trainee);
        return new ResponseEntity<>(newAuth,HttpStatus.CREATED);
    }
    @PostMapping("/update")
    public ResponseEntity<Trainee> updateTrainee(@RequestBody Trainee trainee){
        Trainee newTrainee = traineeService.updateTrainee(trainee);
        return new ResponseEntity<>(newTrainee,HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trainee>> getAllTrainees(){
        return new ResponseEntity<>(traineeService.getAllTrainees(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Trainee> getTrainee(@PathVariable String username){
        return new ResponseEntity<>(traineeService.getTraineeByUserUsername(username), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteTrainee(@PathVariable String username){
        traineeService.deleteByUserUserName(username);
        return new ResponseEntity<>("Deleted user", HttpStatus.OK);
    }



}
