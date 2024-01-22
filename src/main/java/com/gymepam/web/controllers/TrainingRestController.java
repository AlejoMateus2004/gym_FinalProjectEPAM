package com.gymepam.web.controllers;

import com.gymepam.dao.TrainingRepo;
import com.gymepam.domain.Training;
import com.gymepam.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training")
public class TrainingRestController {

    TrainingService trainingService;

    @Autowired
    public TrainingRestController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping
    public ResponseEntity<Training> saveTraining(@RequestBody Training training){
        Training newTraining = trainingService.saveTraining(training);
        return new ResponseEntity<>(newTraining,HttpStatus.CREATED);
    }

    @GetMapping("/TraineeTrainingsList/{username}")
    public ResponseEntity<List<Training>> getTraineeTrainingsList(@PathVariable String username){
        return new ResponseEntity<>(trainingService.getAllTrainingsByTrainee(username), HttpStatus.OK);
    }

    @GetMapping("/TrainerTrainingsList/{username}")
    public ResponseEntity<List<Training>> getTrainerTrainingsList(@PathVariable String username){
        return new ResponseEntity<>(trainingService.getAllTrainingsByTrainer(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Training>> getAllTrainings(){
        return new ResponseEntity<>(trainingService.getAllTrainings(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> getTraining(@PathVariable Long id){
        return new ResponseEntity<>(trainingService.getTraining(id), HttpStatus.OK);
    }

}
