package com.gymepam.service.facade;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.UserRecord;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.mapper.TraineeMapper;
import com.gymepam.mapper.TrainerMapper;
import com.gymepam.service.trainee.TraineeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.trainer.facade.TrainerFacadeService;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.util.GeneratePasswordImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TrainerFacadeServiceTest {

    @Spy
    private GeneratePasswordImpl generatePassword;

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeMapper traineeMapper;

    @Mock
    private TraineeService traineeService;

    @Mock
    TrainerService trainerService;

    @Mock
    TrainingMicroService trainingMicroService;

    @InjectMocks
    private TrainerFacadeService trainerFacadeService;


    @Test
    void save_Trainer_SuccessfulSave_ReturnsAuthenticationRequest() {
        AuthenticationRequest authenticationResponse= new AuthenticationRequest();
        authenticationResponse.setUsername("username");
        authenticationResponse.setPassword("password");

        TrainerRecord.TrainerRequest trainerRequest = new TrainerRecord.TrainerRequest(
                new UserRecord.UserRequest(
                        "Alejandro",
                        "Mateus"
                ),
                null
        );

        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setUserName("pepito.perez");

        trainer.setUser(user);
        when(generatePassword.generatePassword()).thenReturn("generatedPassword");
        doReturn(trainer).when( trainerService ).saveTrainer( any() );
        when( trainerMapper.trainerRequestToTrainer(any()) ).thenReturn(trainer);

        ResponseEntity<AuthenticationRequest> responseEntity = trainerFacadeService.save_Trainer(trainerRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("pepito.perez", Objects.requireNonNull(responseEntity.getBody()).getUsername());
        assertEquals("generatedPassword", responseEntity.getBody().getPassword());
    }

    @Test
    void save_Trainer_FailedSave_ReturnsBadRequest() {
        when(trainerMapper.trainerRequestToTrainer(any())).thenReturn(null);

        TrainerRecord.TrainerRequest trainerRequest = new TrainerRecord.TrainerRequest(
                new UserRecord.UserRequest(
                        "Alejandro",
                        "Mateus"
                ),
                null
        );
        ResponseEntity<AuthenticationRequest> responseEntity = trainerFacadeService.save_Trainer(trainerRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }


    @Test
    public void getTrainerByUserUsername_ValidTest() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setUserName("pepito.perez");
        trainer.setUser(user);
        TrainerRecord.TrainerResponseWithTrainees mockResponse = new TrainerRecord.TrainerResponseWithTrainees(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez",
                        true,
                        "pepito.perez"
                ),
                null,
                Collections.singleton(new TraineeRecord.TraineeResponse(
                        new UserRecord.UserComplete(
                                "Alejandro",
                                "Mateus",
                                true,
                                "alejandro.mateus"
                        )
                ))
        );

        Mockito.when(trainerService.getTrainerByUserUsername("pepito.perez")).thenReturn(trainer);
        Mockito.when(trainerMapper.trainerToTrainerResponseWithTrainees(trainer)).thenReturn(mockResponse);

        TrainerRecord.TrainerResponseWithTrainees result = trainerFacadeService.getTrainerByUserUsername_("pepito.perez");

        Mockito.verify(trainerService).getTrainerByUserUsername("pepito.perez");
        Mockito.verify(trainerMapper).trainerToTrainerResponseWithTrainees(trainer);

        assertEquals(mockResponse, result);
    }

    @Test
    void updateTrainer_ValidTest() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez Guzman");
        user.setUserName("pepito.perez");
        trainer.setUser(user);


        TrainerRecord.TrainerUpdateRequest trainerRequest = new TrainerRecord.TrainerUpdateRequest(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez Guzman",
                        true,
                        "pepito.perez"
                ),
                null
        );

        TrainerRecord.TrainerResponseWithTrainees mockResponse = new TrainerRecord.TrainerResponseWithTrainees(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez Guzman",
                        true,
                        "pepito.perez"
                ),
                null,
                Collections.singleton(new TraineeRecord.TraineeResponse(
                        new UserRecord.UserComplete(
                                "Alejandro",
                                "Mateus",
                                true,
                                "alejandro.mateus"
                        )
                ))
        );
        Mockito.when(trainerMapper.trainerUpdateRequestToTrainer(trainerRequest)).thenReturn(trainer);
        Mockito.when(trainerService.updateTrainer(trainer)).thenReturn(trainer);
        Mockito.when(trainerMapper.trainerToTrainerResponseWithTrainees(trainer)).thenReturn(mockResponse);

        TrainerRecord.TrainerResponseWithTrainees result = trainerFacadeService.updateTrainer_(trainerRequest);

        Mockito.verify(trainerMapper).trainerUpdateRequestToTrainer(trainerRequest);
        Mockito.verify(trainerService).updateTrainer(trainer);
        Mockito.verify(trainerMapper).trainerToTrainerResponseWithTrainees(trainer);

        assertEquals(mockResponse, result);
    }



    @Test
    public void givenValidRequestAndAllServicesReturnCorrectly_whenGetTrainerByUserUsernameWithTrainingParams_thenOkResponse() {
        Trainer trainer = new Trainer();
        User user1 = new User();
        user1.setUserName("trainer.username");
        trainer.setUser(user1);

        Trainee trainee = new Trainee();
        User user2 = new User();
        user2.setUserName("trainee.username");
        trainee.setUser(user1);

        TrainingRecord.TrainerTrainingParamsRequest request = new TrainingRecord.TrainerTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );

        GlobalModelResponse expectedResponse = new GlobalModelResponse();
        expectedResponse.setResponse(new TrainingRecord.TrainerTrainingResponse(
                1L,
                "TEST1",
                LocalDate.parse("2024-05-06"),
                trainer.getUser().getUserName()
        ));

        when(trainerService.getTrainerByUserUsername(request.trainerUsername())).thenReturn(trainer);
        when(traineeService.getTraineeByUserUsername(request.traineeUsername())).thenReturn(trainee);
        when(trainingMicroService.getTrainerTrainingListByTrainingParams(request)).thenReturn(ResponseEntity.ok(expectedResponse));


        ResponseEntity<GlobalModelResponse> response = trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(traineeService).getTraineeByUserUsername(request.traineeUsername());
        verify(trainerService).getTrainerByUserUsername(request.trainerUsername());
        verify(trainingMicroService).getTrainerTrainingListByTrainingParams(request);
    }

    @Test
    public void givenInvalidTrainerRequest_whenGetTrainerByUserUsernameWithTrainingParams_thenBadRequest() {
        ResponseEntity<GlobalModelResponse> response = trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButInvalidTrainer_whenGetTrainerByUserUsernameWithTrainingParams_thenNotFound() {
        TrainingRecord.TrainerTrainingParamsRequest request = new TrainingRecord.TrainerTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );

        when(trainerService.getTrainerByUserUsername("trainer.username")).thenReturn(null);

        ResponseEntity<GlobalModelResponse> response = trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButInvalidTrainer_whenGetTraineeByUserUsernameWithTrainingParams_thenNotFound() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUserName("trainer.username");
        trainer.setUser(user);


        TrainingRecord.TrainerTrainingParamsRequest request = new TrainingRecord.TrainerTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );


        when(trainerService.getTrainerByUserUsername(trainer.getUser().getUserName())).thenReturn(trainer);
        when(traineeService.getTraineeByUserUsername("trainee.username")).thenReturn(null);

        ResponseEntity<GlobalModelResponse> response = trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButMicroserviceError_whenGetTrainerByUserUsernameWithTrainingParams_thenBadRequest() {
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUserName("trainer.username");
        trainer.setUser(user);

        TrainingRecord.TrainerTrainingParamsRequest request = new TrainingRecord.TrainerTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                ""
        );

        when(trainerService.getTrainerByUserUsername(trainer.getUser().getUserName())).thenReturn(trainer);
        when(trainingMicroService.getTrainerTrainingListByTrainingParams(request)).thenThrow(new RuntimeException());

        ResponseEntity<GlobalModelResponse> response = trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdateStatus_thenTraineeNotFound() {
        String username = "sampleUsername";
        boolean isActive = true;

        when(trainerService.getTrainerByUserUsername(username)).thenReturn(null);

        ResponseEntity result = trainerFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void whenUpdateStatus_thenBadUpdate() {
        String username = "sampleUsername";
        boolean isActive = true;

        Trainer trainer = new Trainer();
        User user = new User();
        trainer.setUser(user);

        when(trainerService.getTrainerByUserUsername(username)).thenReturn(trainer);
        when(trainerService.updateTrainer(trainer)).thenReturn(null);

        ResponseEntity result = trainerFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void whenUpdateStatus_thenReturnOk() {
        String username = "sampleUsername";
        boolean isActive = true;

        Trainer trainer = new Trainer();
        User user = new User();
        Trainer updatedTrainer = new Trainer();
        trainer.setUser(user);

        when(trainerService.getTrainerByUserUsername(username)).thenReturn(trainer);
        when(trainerService.updateTrainer(trainer)).thenReturn(updatedTrainer);

        ResponseEntity result = trainerFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}