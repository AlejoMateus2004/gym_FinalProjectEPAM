package com.gymepam.web.controllers;

import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;
import com.gymepam.service.Trainee2TrainerService;
import com.gymepam.service.TraineeService;
import com.gymepam.service.facade.Trainee2TrainerFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/trainee2trainer")
public class Trainee2TrainerRestController {

    Trainee2TrainerFacadeService trainee2TrainerService;

    @Autowired
    public Trainee2TrainerRestController(Trainee2TrainerFacadeService trainee2TrainerService) {
        this.trainee2TrainerService = trainee2TrainerService;
    }

    @PostMapping("/save")
    public ResponseEntity<Trainee2Trainer> save(@RequestBody Map<String,String> trainee2Trainer){
        var tr = trainee2TrainerService.save(trainee2Trainer);
        return new ResponseEntity<>(tr,HttpStatus.CREATED);
    }

    @GetMapping("/trainer-list/{username}")
    public ResponseEntity<List<Trainee2Trainer>> getTrainerListByTrainee(@PathVariable String username){
        return new ResponseEntity<>(trainee2TrainerService.getTrainerListByTrainee(username), HttpStatus.OK);
    }

}
