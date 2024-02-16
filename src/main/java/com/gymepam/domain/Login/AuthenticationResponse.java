package com.gymepam.domain.Login;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String username;
    private String role;
    private String jwt;

}
