package com.gymepam.service.feignClients;

import com.gymepam.domain.dto.records.TrainingRecord.TrainingSummary;
import com.gymepam.domain.dto.records.TrainingRecord;
import feign.FeignException;
import feign.Headers;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "training-microservice", url = "http://localhost:8083",path = "/training",fallback = TrainingFallBack.class)
public interface TrainingFeignClient {

    @PostMapping
    ResponseEntity saveTraining(@RequestBody @Validated TrainingRecord.TrainingRequest trainingRequest);

    @PutMapping("/status/{trainingId}")
    ResponseEntity updateTrainingStatusToCompleted(@PathVariable Long trainingId);

    @PostMapping("/list/trainer")
    ResponseEntity<List<TrainingRecord.TrainerTrainingResponse>> getTrainerTrainingListByTrainingParams(@RequestBody TrainingRecord.TrainerTrainingParamsRequest trainingParams);

    @PostMapping("/list/trainee")
    ResponseEntity<List<TrainingRecord.TraineeTrainingResponse>> getTraineeTrainingListByTrainingParams(@RequestBody TrainingRecord.TraineeTrainingParamsRequest trainingParams);

    @GetMapping("/summary/trainer/{trainerUsername}")
    ResponseEntity<TrainingSummary> getTrainingSummaryByTrainerUsername(@PathVariable String trainerUsername);

    @DeleteMapping("/{trainingId}")
    ResponseEntity<Void> deleteTrainingById(@PathVariable Long trainingId);

}
