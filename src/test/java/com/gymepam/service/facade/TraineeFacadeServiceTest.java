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
import com.gymepam.service.trainee.facade.TraineeFacadeService;
import com.gymepam.service.trainer.TrainerService;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.util.GeneratePasswordImpl;
import org.junit.jupiter.api.Assertions;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.junit.Assert.*;
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

    @Test
    public void getTraineeByUserUsername_ValidTest() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez");
        user.setUserName("pepito.perez");
        trainee.setUser(user);
        TraineeRecord.TraineeResponseWithTrainers mockResponse = new TraineeRecord.TraineeResponseWithTrainers(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez",
                        true,
                        "pepito.perez"
                ),
                LocalDate.parse("2004-08-06"),
                "Cra 13 # 1-33",
                Collections.singleton(new TrainerRecord.TrainerResponse(
                        new UserRecord.UserComplete(
                                "Alejandro",
                                "Mateus",
                                true,
                                "alejandro.mateus"
                        ),
                        null
                ))
        );

        Mockito.when(traineeService.getTraineeByUserUsername("pepito.perez")).thenReturn(trainee);
        Mockito.when(traineeMapper.traineeToTraineeResponseWithTrainers(trainee)).thenReturn(mockResponse);

        TraineeRecord.TraineeResponseWithTrainers result = traineeFacadeService.getTraineeByUserUsername_("pepito.perez");

        Mockito.verify(traineeService).getTraineeByUserUsername("pepito.perez");
        Mockito.verify(traineeMapper).traineeToTraineeResponseWithTrainers(trainee);

        assertEquals(mockResponse, result);
    }

    @Test
    void updateTrainee_ValidTest() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setFirstName("Pepito");
        user.setLastName("Perez Guzman");
        user.setUserName("pepito.perez");
        trainee.setUser(user);


        TraineeRecord.TraineeUpdateRequest traineeRequest = new TraineeRecord.TraineeUpdateRequest(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez Guzman",
                        true,
                        "pepito.perez"
                ),
                LocalDate.parse("2004-08-06"),
                "Cra 13 # 1-33"
        );

        TraineeRecord.TraineeResponseWithTrainers mockResponse = new TraineeRecord.TraineeResponseWithTrainers(
                new UserRecord.UserComplete(
                        "Pepito",
                        "Perez Guzman",
                        true,
                        "pepito.perez"
                ),
                LocalDate.parse("2004-08-06"),
                "Cra 13 # 1-33",
                Collections.singleton(new TrainerRecord.TrainerResponse(
                        new UserRecord.UserComplete(
                                "Alejandro",
                                "Mateus",
                                true,
                                "alejandro.mateus"
                        ),
                        null
                ))
        );
        Mockito.when(traineeMapper.traineeUpdateRequestToTrainee(traineeRequest)).thenReturn(trainee);
        Mockito.when(traineeService.updateTrainee(trainee)).thenReturn(trainee);
        Mockito.when(traineeMapper.traineeToTraineeResponseWithTrainers(trainee)).thenReturn(mockResponse);

        TraineeRecord.TraineeResponseWithTrainers result = traineeFacadeService.updateTrainee_(traineeRequest);

        Mockito.verify(traineeMapper).traineeUpdateRequestToTrainee(traineeRequest);
        Mockito.verify(traineeService).updateTrainee(trainee);
        Mockito.verify(traineeMapper).traineeToTraineeResponseWithTrainers(trainee);

        assertEquals(mockResponse, result);
    }

    @Test
    public void givenValidRequestAndAllServicesReturnCorrectly_whenGetTraineeByUserUsernameWithTrainingParams_thenOkResponse() {
        // Asigna datos de prueba
        Trainee trainee = new Trainee();
        User user1 = new User();
        user1.setUserName("trainee.username");
        trainee.setUser(user1);

        Trainer trainer = new Trainer();
        User user2 = new User();
        user2.setUserName("trainer.username");
        trainer.setUser(user1);

        TrainingRecord.TraineeTrainingParamsRequest request = new TrainingRecord.TraineeTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );

        GlobalModelResponse expectedResponse = new GlobalModelResponse();
        expectedResponse.setResponse(new TrainingRecord.TraineeTrainingResponse(
                1L,
                "TEST1",
                LocalDate.parse("2024-05-06"),
                trainer.getUser().getUserName()
        ));

        when(traineeService.getTraineeByUserUsername(request.traineeUsername())).thenReturn(trainee);
        when(trainerService.getTrainerByUserUsername(request.trainerUsername())).thenReturn(trainer);
        when(trainingMicroService.getTraineeTrainingListByTrainingParams(request)).thenReturn(ResponseEntity.ok(expectedResponse));


        ResponseEntity<GlobalModelResponse> response = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(traineeService).getTraineeByUserUsername(request.traineeUsername());
        verify(trainerService).getTrainerByUserUsername(request.trainerUsername());
        verify(trainingMicroService).getTraineeTrainingListByTrainingParams(request);
    }
    @Test
    public void givenInvalidTraineeRequest_whenGetTraineeByUserUsernameWithTrainingParams_thenBadRequest() {
        ResponseEntity<GlobalModelResponse> response = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(null);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButInvalidTrainee_whenGetTraineeByUserUsernameWithTrainingParams_thenNotFound() {
        TrainingRecord.TraineeTrainingParamsRequest request = new TrainingRecord.TraineeTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );

        when(traineeService.getTraineeByUserUsername("trainee.username")).thenReturn(null);

        ResponseEntity<GlobalModelResponse> response = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButInvalidTrainer_whenGetTraineeByUserUsernameWithTrainingParams_thenNotFound() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUserName("trainee.username");
        trainee.setUser(user);


        TrainingRecord.TraineeTrainingParamsRequest request = new TrainingRecord.TraineeTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "trainer.username",
                "trainee.username"
        );


        when(traineeService.getTraineeByUserUsername(trainee.getUser().getUserName())).thenReturn(trainee);
        when(trainerService.getTrainerByUserUsername("trainer.username")).thenReturn(null);

        ResponseEntity<GlobalModelResponse> response = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void givenValidRequestButMicroserviceError_whenGetTraineeByUserUsernameWithTrainingParams_thenBadRequest() {
        Trainee trainee = new Trainee();
        User user = new User();
        user.setUserName("trainee.username");
        trainee.setUser(user);

        TrainingRecord.TraineeTrainingParamsRequest request = new TrainingRecord.TraineeTrainingParamsRequest(
                LocalDate.parse("2004-01-06"),
                LocalDate.parse("2024-08-06"),
                "",
                "trainee.username"
        );

        when(traineeService.getTraineeByUserUsername(trainee.getUser().getUserName())).thenReturn(trainee);
        when(trainingMicroService.getTraineeTrainingListByTrainingParams(request)).thenThrow(new RuntimeException());

        ResponseEntity<GlobalModelResponse> response = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdateTraineesTrainerList_thenTraineeNotFound() {
        String username = "sampleUsername";
        Set<String> trainerUsernames = new HashSet<>();

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(null);

        Set<TrainerRecord.TrainerResponse> result = traineeFacadeService.updateTraineesTrainerList(username, trainerUsernames);

        Assertions.assertNull(result);
        verify(traineeService).getTraineeByUserUsername(username);
    }

    @Test
    public void whenUpdateTraineesTrainerList_thenEverythingIsOK() {

        String username = "sampleUsername";
        Set<String> trainerUsernames = new HashSet<>();
        trainerUsernames.add("pepito.perez");

        Trainee trainee = new Trainee();
        trainee.setTrainerList(new HashSet<>());

        Trainer trainer1 = new Trainer();

        Trainee updatedTrainee = new Trainee();

        TraineeRecord.TraineeResponseWithTrainers traineeResponse = new TraineeRecord.TraineeResponseWithTrainers(
                new UserRecord.UserComplete(
                        "trainee",
                        "string",
                        true,
                        "sampleUsername"
                ),
                null,
                "",
                new HashSet<>(
                        Collections.singletonList(new TrainerRecord.TrainerResponse(
                                new UserRecord.UserComplete(
                                        "pepito",
                                        "perez",
                                        true,
                                        "pepito.perez"
                                ),
                                null
                        ))
                )
        );

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(trainerService.getTrainerByUserUsername("pepito.perez")).thenReturn(trainer1);
        when(traineeService.updateTrainee(trainee)).thenReturn(updatedTrainee);
        when(traineeMapper.traineeToTraineeResponseWithTrainers(updatedTrainee)).thenReturn(traineeResponse);

        Set<TrainerRecord.TrainerResponse> result = traineeFacadeService.updateTraineesTrainerList(username, trainerUsernames);


        verify(traineeService).getTraineeByUserUsername(username);
        verify(trainerService).getTrainerByUserUsername("pepito.perez");
        verify(traineeService).updateTrainee(trainee);
        verify(traineeMapper).traineeToTraineeResponseWithTrainers(updatedTrainee);

        // verifica el resultado
        Assertions.assertNotNull(result);
        assertEquals(traineeResponse.trainerList(), result);
    }

    @Test
    public void whenUpdateStatus_thenTraineeNotFound() {
        String username = "sampleUsername";
        boolean isActive = true;

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(null);

        ResponseEntity<Void> result = traineeFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void whenUpdateStatus_thenBadUpdate() {
        String username = "sampleUsername";
        boolean isActive = true;

        Trainee trainee = new Trainee();
        User user = new User();
        trainee.setUser(user);

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(traineeService.updateTrainee(trainee)).thenReturn(null);

        ResponseEntity<Void> result = traineeFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void whenUpdateStatus_thenReturnOk() {
        String username = "sampleUsername";
        boolean isActive = true;

        Trainee trainee = new Trainee();
        User user = new User();
        Trainee updatedTrainee = new Trainee();
        trainee.setUser(user);

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);
        when(traineeService.updateTrainee(trainee)).thenReturn(updatedTrainee);

        ResponseEntity<Void> result = traineeFacadeService.updateStatus(username, isActive);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void whenGetNotAssignedTrainers_thenCorrectlyMapped() {

        String username = "sampleUsername";

        Trainer trainer1 = new Trainer();
        Set<Trainer> trainerSet = new HashSet<>();
        trainerSet.add(trainer1);

        TrainerRecord.TrainerResponse trainerResponse = new TrainerRecord.TrainerResponse(
                new UserRecord.UserComplete(
                        "trainee",
                        "string",
                        true,
                        "trainee.string"
                ),
                null
        );

        when(trainerService.getActiveTrainersNotAssignedToTrainee(username)).thenReturn(trainerSet);
        when(trainerMapper.trainerToTrainerResponse(trainer1)).thenReturn(trainerResponse);

        Set<TrainerRecord.TrainerResponse> result = traineeFacadeService.getNotAssignedTrainersByTraineeUserUsername(username);

        verify(trainerService).getActiveTrainersNotAssignedToTrainee(username);
        verify(trainerMapper).trainerToTrainerResponse(trainer1);

        assertEquals(1, result.size());
        Assertions.assertTrue(result.contains(trainerResponse));
    }

    @Test
    public void whenDeleteTraineeByUserName_thenServiceMethodCalled() {
        String username = "sampleUsername";
        Trainee trainee = new Trainee();

        when(traineeService.getTraineeByUserUsername(username)).thenReturn(trainee);

        traineeFacadeService.deleteTraineeByUserName(username);

        verify(traineeService).getTraineeByUserUsername(username);
        verify(traineeService).deleteTrainee(trainee);
    }
}