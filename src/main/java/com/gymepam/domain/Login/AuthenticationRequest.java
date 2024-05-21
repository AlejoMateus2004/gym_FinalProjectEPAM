package com.gymepam.domain.Login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}