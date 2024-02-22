package com.gymepam.service.facade;

import com.gymepam.config.JwtUtil;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginFacadeServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private JwtUtil jwtUtil;

    MeterRegistry meterRegistry;

    @Mock
    private UserService userService;

    @Mock
    private Counter sessionCounter;

    private LoginFacadeService loginFacadeService;

    private LoginFacadeService.ChangeLoginRequest changeLoginRequest;

    @BeforeEach
    void setUp() {
        meterRegistry = spy(new SimpleMeterRegistry());
        loginFacadeService = new LoginFacadeService(authenticationManager, jwtUtil, traineeService, trainerService, meterRegistry, userService);
        changeLoginRequest = new LoginFacadeService.ChangeLoginRequest("username","oldPassword","newPassword");
    }

    @Test
    void getAuthenticationResponse_SuccessfulAuthentication_ReturnsJwtToken() {
        // AAA, GTW
        // given | arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("username");
        request.setPassword("password");
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.create("username")).thenReturn("fakeJwtToken");
//        when(meterRegistry.counter("sessionCounter")).thenReturn(sessionCounter);

        // when | Act
        ResponseEntity<AuthenticationResponse> responseEntity = loginFacadeService.getAuthenticationResponse(request);

        // then | assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("username", responseEntity.getBody().getUsername());
        assertEquals("fakeJwtToken", responseEntity.getBody().getJwt());

    }

    @Test
    void getAuthenticationResponse_BadCredentials_ReturnsForbidden() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("username");
        request.setPassword("password");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<AuthenticationResponse> responseEntity = loginFacadeService.getAuthenticationResponse(request);

        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    void changeAuthentication_ValidTraineeUsernameAndPassword_ReturnsOk() {
        when(traineeService.getTraineeByUserUsername("username")).thenReturn(new Trainee());
        when(traineeService.updatePassword(changeLoginRequest.username(), changeLoginRequest.oldPassword(), changeLoginRequest.newPassword())).thenReturn(new Trainee());

        ResponseEntity<Void> responseEntity = loginFacadeService.changeAuthentication(changeLoginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void changeAuthentication_ValidTrainerUsernameAndPassword_ReturnsOk() {

        when(traineeService.getTraineeByUserUsername("username")).thenReturn(null);
        when(trainerService.getTrainerByUserUsername("username")).thenReturn(new Trainer());
        when(trainerService.updatePassword(changeLoginRequest.username(), changeLoginRequest.oldPassword(), changeLoginRequest.newPassword())).thenReturn(new Trainer());



        ResponseEntity<Void> responseEntity = loginFacadeService.changeAuthentication(changeLoginRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void changeAuthentication_InvalidUsername_ReturnsBadRequest() {
        changeLoginRequest = new LoginFacadeService.ChangeLoginRequest("nonexistentUser","oldPassword","newPassword");

        ResponseEntity<Void> responseEntity = loginFacadeService.changeAuthentication(changeLoginRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
}