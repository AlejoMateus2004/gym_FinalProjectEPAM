package com.gymepam.domain.dto.records;

import com.gymepam.domain.entities.TrainingType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class TrainingRecord {

    public record TrainingFilterRequest(
        LocalDate periodFrom,
        LocalDate periodTo,
        String user_name,
        String training_type
        ){
    }

    public record TraineeTrainingResponse(
            String trainingName,
            LocalDate trainingDate,
            TrainerRecord.TrainerUserResponse trainer,
            TrainingType trainingType
    ){
    }
    public record TrainerTrainingResponse(
            String trainingName,
            LocalDate trainingDate,
            TraineeRecord.TraineeUserResponse trainee,
            TrainingType trainingType
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
            ){}

}
