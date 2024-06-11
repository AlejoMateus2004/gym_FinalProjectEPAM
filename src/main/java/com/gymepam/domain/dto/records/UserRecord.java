package com.gymepam.domain.dto.records;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRecord {

    public record UserRequest(
            @NotBlank(message = "Firstname can't be null or empty")
            String firstName,
            @NotBlank(message = "Lastname can't be null or empty")
            String lastName
    ){
    }

    public record UserComplete(
            @NotBlank(message = "Firstname can't be null or empty")
            String firstName,
            @NotBlank(message = "Lastname can't be null or empty")
            String lastName,
            @NotNull(message = "Status can't be null or empty")
            boolean isActive,
            @NotBlank(message = "Username can't be null or empty")
            String userName
    ){
    }


}
