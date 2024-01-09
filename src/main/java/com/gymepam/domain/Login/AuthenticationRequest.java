package com.gymepam.domain.Login;

import com.gymepam.domain.Roles;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class AuthenticationRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}