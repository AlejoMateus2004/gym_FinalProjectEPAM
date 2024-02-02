package com.gymepam.domain.dto.records;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
            @NotNull(message = "Firstname can't be null or empty")
            boolean isActive,
            @NotBlank(message = "Username can't be null or empty")
            String userName
    ){
    }


}
