package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.service.facade.LoginFacadeService;
import com.gymepam.service.facade.LoginFacadeService.ChangeLoginRequest;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Authentication Controller", value = "Operations for login, and change user password in the application")
@RestController
@RequestMapping("/auth")
public class AuthRestController {

    LoginFacadeService loginFacadeService;

    Counter sessionCounter;

    public AuthRestController(MeterRegistry meterRegistry, LoginFacadeService loginFacadeService) {
        this.sessionCounter = Counter.builder("sessionCounter")
                .description("Number Of sessions").register(meterRegistry);

        this.loginFacadeService = loginFacadeService;
    }

    @ApiOperation(value = "Login", notes = "Log in to the systems")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authenticationRequest) {
        sessionCounter.increment();
        return loginFacadeService.getAuthenticationResponse(authenticationRequest);

    }

    @ApiOperation(value = "Change Login", notes = "Change password")
    @ApiImplicitParam(name = "Authorization", value = "Authorization Token Bearer", required = true,
            dataTypeClass = String.class, paramType = "header", example = "Bearer")
    @PutMapping("/login")
    public ResponseEntity changeLogin(@RequestBody @Validated ChangeLoginRequest authenticationRequest) {
        return loginFacadeService.changeAuthentication(authenticationRequest);
    }

}
