package com.gymepam.domain.dto.records;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class TraineeRecord {

    public record TraineeRequest(
            UserRecord.UserRequest user,
            LocalDate dateOfBirth,
            String address
    ) {
    }

    public record TraineeUpdateRequest(
            UserRecord.UserComplete user,
            LocalDate dateOfBirth,
            String address
    ) {
    }

    public record TraineeResponseWithTrainers(
            UserRecord.UserComplete user,
            LocalDate dateOfBirth,
            String address,
            Set<TrainerRecord.TrainerResponse> trainerList
            ){
    }
    public record TraineeResponse(
            UserRecord.UserComplete user
    ) {
    }

    public record TraineeUserResponse(
            UserRecord.UserRequest user
    ) {
    }

    public record TraineeTrainerList(
            @NotBlank(message = "Trainee username can't be null or empty")
            String trainee_username,
            @NotEmpty(message = "List of trainer usernames can't be null or empty")
            Set<String> trainerUsernames
    ){
    }

    public record TraineeRequestWithTrainingParams(
            @NotBlank(message = "Trainee username can't be null or empty")
            String trainee_username,
            TrainingRecord.TrainingFilterRequest trainingRequest
    ){
    }
    public record TraineeResponseWithTrainings(
            List<TrainingRecord.TraineeTrainingResponse> trainingList
    ){
    }
}
