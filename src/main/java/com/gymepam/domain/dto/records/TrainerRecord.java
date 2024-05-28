package com.gymepam.domain.dto.records;

import com.gymepam.domain.entities.TrainingType;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class TrainerRecord {
    public record TrainerRequest(
            UserRecord.UserRequest user,
            TrainingType trainingType
    )implements Serializable {
    }

    public record TrainerUpdateRequest(
            UserRecord.UserComplete user,
            TrainingType trainingType
    )implements Serializable {
    }

    public record TrainerResponseWithTrainees(
            UserRecord.UserComplete user,
            TrainingType trainingType,
            Set<TraineeRecord.TraineeResponse> traineeList
    )implements Serializable{
    }
    public record TrainerResponse(
            UserRecord.UserComplete user,
            TrainingType trainingType
    ) implements Serializable{
    }

    public record TrainerRequestWithTrainingParams(
            @NotBlank(message = "Trainer username can't be null or empty")
            String trainer_username,
            TrainingRecord.TrainingFilterRequest trainingRequest
    )implements Serializable{
    }
    public record TrainerResponseWithTrainings(
            List<TrainingRecord.TrainerTrainingResponse> trainingList
    )implements Serializable{
    }
}
