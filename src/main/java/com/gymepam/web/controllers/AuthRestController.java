package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.service.facade.LoginFacadeService;
import com.gymepam.service.facade.LoginFacadeService.ChangeLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication Controller", description = "Operations for login, and change user password in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    private LoginFacadeService loginFacadeService;

    @Operation(summary = "Login", description = "Log in to the systems")
    @PostMapping("/public/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authenticationRequest) {

        return loginFacadeService.getAuthenticationResponse(authenticationRequest);

    }

    @Operation(summary = "Change Login", description = "Change password")
    @PutMapping("/login")
    public ResponseEntity changeLogin(@RequestBody @Validated ChangeLoginRequest authenticationRequest) {
        return loginFacadeService.changeAuthentication(authenticationRequest);
    }

}
