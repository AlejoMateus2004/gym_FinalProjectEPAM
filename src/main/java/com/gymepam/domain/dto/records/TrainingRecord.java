package com.gymepam.domain.dto.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Map;

public class TrainingRecord {

    public record TrainingFilterRequest(
        LocalDate periodFrom,
        LocalDate periodTo,
        String user_name
        ){
    }
    public record TrainerTrainingParamsRequest(
            LocalDate periodFrom,
            LocalDate periodTo,
            @NotBlank(message = "Trainer username can't be null or empty")
            String trainerUsername,
            String traineeUsername
    ){
    }
    public record TraineeTrainingParamsRequest(
            LocalDate periodFrom,
            LocalDate periodTo,
            String trainerUsername,
            @NotBlank(message = "Trainee username can't be null or empty")
            String traineeUsername
    ){
    }

    public record TraineeTrainingResponse(
            Long id,
            String trainingName,
            LocalDate trainingDate,
            String trainerUsername
    ){
    }

    public record TrainerTrainingResponse(
            Long id,
            String trainingName,
            LocalDate trainingDate,
            String traineeUsername
    ){
    }
    public record TrainerDetailsTrainingSummary(
            TrainerRecord.TrainerResponse trainer,
            Map<Integer, Map<String, Long>> summary
    ){
    }

    public record TrainingSummary (
            Map<Integer, Map<String, Long>> summary
    ){
    }

    public record TrainingRequest(
            @NotBlank(message = "Trainee username can't be null or empty")
            String traineeUsername,
            @NotBlank(message = "Trainer username can't be null or empty")
            String trainerUsername,
            @NotBlank(message = "Training Name can't be null or empty")
            String trainingName,
            @NotNull(message = "Training Date can't be null or empty")
            LocalDate trainingDate,
            @NotNull(message = "Training Duration can't be null or empty")
            Long trainingDuration
            ){
    }
}
