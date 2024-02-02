package com.gymepam.service.facade;

import com.gymepam.config.JwtUtil;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Login.AuthenticationResponse;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Slf4j
@AllArgsConstructor
@Service
public class LoginFacadeService {


    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private TraineeService traineeService;
    private TrainerService trainerService;


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
            authenticationManager.authenticate(login);

            String userName = authenticationRequest.getUsername();

            String jwt = jwtUtil.create(userName);

            AuthenticationResponse response = new AuthenticationResponse();
            response.setUsername(userName);
            response.setJwt(jwt);
            response.setRole(login.getAuthorities().toString());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (BadCredentialsException e) {
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
            isUpdated = traineeService.updatePassword(username,oldPassword,newPassword) != null ? true : false;
        } else {
            Trainer trainer = trainerService.getTrainerByUserUsername(username);
            if (trainer != null) {
                isUpdated = trainerService.updatePassword(username,oldPassword,newPassword) != null ? true : false;
            }
        }

        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();

    }
}
