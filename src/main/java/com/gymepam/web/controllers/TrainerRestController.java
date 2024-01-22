package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Trainer;
import com.gymepam.service.TrainerService;
import com.gymepam.service.facade.TrainerFacadeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/trainer")
public class TrainerRestController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerRestController.class);

    TrainerFacadeService trainerFacade;
    TrainerService trainerService;


    @Autowired
    public TrainerRestController(TrainerService trainerService, TrainerFacadeService trainerFacade) {
        this.trainerService = trainerService;
        this.trainerFacade = trainerFacade;
    }

    @PostMapping
    public ResponseEntity<AuthenticationRequest> saveTrainer(@RequestBody @Valid Trainer trainer, Errors errors){
        if (errors.hasErrors()) {
            logger.error("Validation Error " + errors.getFieldError().getDefaultMessage());
            return ResponseEntity.badRequest().body(null);
        }

        AuthenticationRequest newAuth = trainerFacade.saveTrainer(trainer);
        return new ResponseEntity<>(newAuth,HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Trainer>> getAllTrainers(){
        return new ResponseEntity<>(trainerService.getAllTrainers(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Trainer> getTrainer(@PathVariable String username){
        return new ResponseEntity<>(trainerService.getTrainerByUserUsername(username), HttpStatus.OK);
    }

    @GetMapping("/allNotAssigned")
    public ResponseEntity<List<Trainer>> getAllTrainersNotAssignedToActiveTraineeList(){
        return new ResponseEntity<>(trainerService.getTrainerByTraineeListEmpty(), HttpStatus.OK);
    }


}
