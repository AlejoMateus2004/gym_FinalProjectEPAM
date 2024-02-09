package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.service.facade.LoginFacadeService;
import com.gymepam.service.facade.LoginFacadeService.ChangeLoginRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Authentication Controller", value = "Operations for login, and change user password in the application")
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthRestController {

    LoginFacadeService loginFacadeService;

    @GetMapping("/public/test")
    public ResponseEntity<Void> test() throws Exception {
        log.info("Invocando mi servicio");
        if(true){
            throw new Exception("Error de pruebas");
        }
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Login", notes = "Log in to the systems")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Validated AuthenticationRequest authenticationRequest) {
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
