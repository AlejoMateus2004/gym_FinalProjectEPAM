package com.gymepam.domain.dto.records;

import com.gymepam.domain.entities.TrainingType;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

public class TrainerRecord {
    public record TrainerRequest(
            UserRecord.UserRequest user,
            TrainingType trainingType
    ) {
    }

    public record TrainerUpdateRequest(
            UserRecord.UserComplete user,
            TrainingType trainingType
    ) {
    }

    public record TrainerResponseWithTrainees(
            UserRecord.UserComplete user,
            TrainingType trainingType,
            Set<TraineeRecord.TraineeResponse> traineeList
    ){
    }
    public record TrainerResponse(
            UserRecord.UserComplete user,
            TrainingType trainingType
    ) {
    }

    public record TrainerRequestWithTrainingParams(
            @NotBlank(message = "Trainer username can't be null or empty")
            String trainer_username,
            TrainingRecord.TrainingFilterRequest trainingRequest
    ){
    }
    public record TrainerResponseWithTrainings(
            List<TrainingRecord.TrainerTrainingResponse> trainingList
    ){
    }
}
