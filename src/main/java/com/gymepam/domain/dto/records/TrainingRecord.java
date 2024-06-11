package com.gymepam.domain.dto.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

public class TrainingRecord {

    public record TrainingFilterRequest(
        LocalDate periodFrom,
        LocalDate periodTo,
        String user_name
    ) implements Serializable {
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
    ) implements Serializable{
    }

    public record TraineeTrainingResponse(
        Long id,
        String trainingName,
        LocalDate trainingDate,
        String trainerUsername
    ) implements Serializable{
    }

    public record TrainerTrainingResponse(
        Long id,
        String trainingName,
        LocalDate trainingDate,
        String traineeUsername
    ) implements Serializable{
    }
    public record TrainerDetailsTrainingSummary(
        TrainerRecord.TrainerResponse trainer,
        Map<Integer, Map<String, Long>> summary
    ) implements Serializable{
    }

    public record TrainingSummary (
        String trainerUsername,
        String trainerFirstName,
        String trainerLastName,
        Boolean trainerStatus,
        Map<Integer, Map<String, Long>> summary
    ) implements Serializable{
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
    ) implements Serializable{
    }
    public record TrainingMicroserviceRequest(
        @NotBlank(message = "Trainer Firstname can't be null or empty")
        String trainerFirstName,
        @NotBlank(message = "Trainer Lastname can't be null or empty")
        String trainerLastName,
        @NotBlank(message = "Trainer Username can't be null or empty")
        String trainerUsername,
        @NotNull(message = "Trainer status can't be null or empty")
        boolean trainerStatus,
        @NotBlank(message = "Trainee username can't be null or empty")
        String traineeUsername,
        @NotBlank(message = "Training Name can't be null or empty")
        String trainingName,
        @NotNull(message = "Training Date can't be null or empty")
        LocalDate trainingDate,
        @NotNull(message = "Training Duration can't be null or empty")
        Long trainingDuration
    ) implements Serializable{
    }
}
