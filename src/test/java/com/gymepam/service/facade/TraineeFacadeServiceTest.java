package com.gymepam.service.facade;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.UserRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TraineeMapper;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainee.facade.TraineeFacadeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.training.feignClients.TrainingFeignClient;
import com.gymepam.service.util.GeneratePasswordImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeFacadeServiceTest {

    @Spy
    private GeneratePasswordImpl generatePassword;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TrainerService trainerService;

    @Mock
    TraineeService traineeService;

    @Mock
    TrainingMicroService trainingMicroService;

    @InjectMocks
    private TraineeFacadeService traineeFacadeService;


    @Test
    void save_Trainee_SuccessfulSave_ReturnsAuthenticationRequest() {
        AuthenticationRequest authenticationResponse= new AuthenticationRequest();
        authenticationResponse.setUsername("username");
        authenticationResponse.setPassword("password");

        TraineeRecord.TraineeRequest traineeRequest = new TraineeRecord.TraineeRequest(
                new UserRecord.UserRequest(
                        "Alejandro",
                        "Mateus"
                ),
                LocalDate.parse("2004-08-06"),
                "Cra 13 # 1-33"
        );

        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setUserName("pepito.perez");

        trainee.setUser(user);
        when(generatePassword.generatePassword()).thenReturn("generatedPassword");
        doReturn(trainee).when( traineeService ).saveTrainee( any() );
        when( traineeMapper.traineeRequestToTrainee(any()) ).thenReturn(trainee);

        ResponseEntity<AuthenticationRequest> responseEntity = traineeFacadeService.save_Trainee(traineeRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("pepito.perez", Objects.requireNonNull(responseEntity.getBody()).getUsername());
        assertEquals("generatedPassword", responseEntity.getBody().getPassword());
    }

    @Test
    void save_Trainee_FailedSave_ReturnsBadRequest() {
        Trainee trainee = new Trainee();
        when(traineeMapper.traineeRequestToTrainee(any())).thenReturn(null);


        TraineeRecord.TraineeRequest traineeRequest = new TraineeRecord.TraineeRequest(
                new UserRecord.UserRequest(
                        "Alejandro",
                        "Mateus"
                ),
                LocalDate.parse("2004-08-06"),
                "Cra 13 # 1-33"
        );
        ResponseEntity<AuthenticationRequest> responseEntity = traineeFacadeService.save_Trainee(traineeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


}