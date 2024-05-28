package com.gymepam.domain.dto.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TraineeRecord {

    public record TraineeRequest(
            UserRecord.UserRequest user,
            LocalDate dateOfBirth,
            String address
    ) implements Serializable {
    }

    public record TraineeUpdateRequest(
            UserRecord.UserComplete user,
            LocalDate dateOfBirth,
            String address
    ) implements Serializable {
    }

    public record TraineeResponseWithTrainers(
            UserRecord.UserComplete user,
            LocalDate dateOfBirth,
            String address,
            Set<TrainerRecord.TrainerResponse> trainerList
    )implements Serializable{
    }
    public record TraineeResponse(
            UserRecord.UserComplete user
    ) {
    }

    public record TraineeTrainerList(
            @NotBlank(message = "Trainee username can't be null or empty")
            String trainee_username,
            @NotEmpty(message = "List of trainer usernames can't be null or empty")
            Set<String> trainerUsernames
    )implements Serializable{
    }

    public record TraineeRequestWithTrainingParams(
            @NotBlank(message = "Trainee username can't be null or empty")
            String trainee_username,
            TrainingRecord.TrainingFilterRequest trainingRequest
    ){
    }
    public record TraineeResponseWithTrainings(
            List<TrainingRecord.TraineeTrainingResponse> trainingList
    )implements Serializable{
    }
}
