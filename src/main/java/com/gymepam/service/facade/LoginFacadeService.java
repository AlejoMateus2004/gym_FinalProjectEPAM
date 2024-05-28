package com.gymepam.service.facade;

import com.gymepam.config.JwtUtil;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.user.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginFacadeService {


    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TraineeService traineeService;
    private final TrainerService trainerService;

    private final UserService userService;
    Counter sessionCounter;
    public LoginFacadeService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, TraineeService traineeService, TrainerService trainerService, MeterRegistry meterRegistry, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.sessionCounter = Counter.builder("sessionCounter")
                .description("Number Of sessions").register(meterRegistry);
        this.userService = userService;
    }



    public record ChangeLoginRequest(
            @NotBlank(message = "Username can't be null or empty")
            String username,
            @NotBlank(message = "Old password can't be null or empty")
            String oldPassword,
            @NotBlank(message = "New Password can't be null or empty")
            String newPassword
    ){
    }
    public ResponseEntity<AuthenticationResponse> getAuthenticationResponse(AuthenticationRequest authenticationRequest) {
        try {
            UsernamePasswordAuthenticationToken login = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(login);
            if (authentication == null) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            String userName = authenticationRequest.getUsername();
            String roles = obtainUserRole(authentication);
            String jwt = jwtUtil.create(userName, roles);

            AuthenticationResponse response = new AuthenticationResponse();
            response.setUsername(userName);
            response.setJwt(jwt);
            response.setRole(login.getAuthorities().toString());
            sessionCounter.increment();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            User user = userService.getUserByUsername(authenticationRequest.getUsername());
            if (user != null) {
                user.incrementFailedLoginAttempts();
                userService.saveUser(user);
            }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Void> changeAuthentication(ChangeLoginRequest changeLoginRequest){

        String username = changeLoginRequest.username;
        String oldPassword = changeLoginRequest.oldPassword;
        String newPassword = changeLoginRequest.newPassword;

        boolean isUpdated = false;
        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        if (trainee != null) {
            isUpdated = traineeService.updatePassword(username, oldPassword, newPassword) != null;
        } else {
            Trainer trainer = trainerService.getTrainerByUserUsername(username);
            if (trainer != null) {
                isUpdated = trainerService.updatePassword(username, oldPassword, newPassword) != null;
            }
        }

        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();

    }


    private String obtainUserRole(Authentication authentication) {
        return authentication.getAuthorities().iterator().next().getAuthority();
    }
}
