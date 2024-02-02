//package com.gymepam.service.facade;
//
//import com.gymepam.domain.Login.AuthenticationRequest;
//import com.gymepam.domain.entities.Trainee;
//import com.gymepam.domain.entities.User;
//import com.gymepam.domain.dto.TraineeDTO;
//import com.gymepam.domain.dto.TrainingDTO;
//import com.gymepam.mapper.TraineeMapper;
//import com.gymepam.service.util.GeneratePassword;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class TraineeFacadeServiceTest {
//
//    @Mock
//    private GeneratePassword generatePassword;
//
//    @Mock
//    private TraineeMapper traineeMapper;
//
//    @InjectMocks
//    private TraineeFacadeService traineeFacadeService;
//
//    @Test
//    void save_Trainee_SuccessfulSave_ReturnsAuthenticationRequest() {
//        Trainee trainee = new Trainee();
//        User user = new User();
//        user.setFirstName("Pepito");
//        user.setLastName("Perez");
//
//        trainee.setUser(user);
//        when(generatePassword.generatePassword()).thenReturn("generatedPassword");
//        when(traineeFacadeService.saveTrainee(trainee)).thenReturn(trainee);
//
//        ResponseEntity<AuthenticationRequest> responseEntity = traineeFacadeService.save_Trainee(trainee);
//
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals("pepito.perez", responseEntity.getBody().getUsername());
//        assertEquals("generatedPassword", responseEntity.getBody().getPassword());
//    }
//
//    @Test
//    void save_Trainee_FailedSave_ReturnsBadRequest() {
//        Trainee trainee = new Trainee();
//        when(generatePassword.generatePassword()).thenReturn("generatedPassword");
//        when(traineeFacadeService.saveTrainee(trainee)).thenReturn(null);
//
//        ResponseEntity<AuthenticationRequest> responseEntity = traineeFacadeService.save_Trainee(trainee);
//
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }
//
//    @Test
//    void getTraineeByUserUsername_Successful_ReturnsTraineeDTO() {
//        String username = "testUsername";
//        Trainee trainee = new Trainee();
//        when(traineeFacadeService.getTraineeByUserUsername(username)).thenReturn(trainee);
//        when(traineeMapper.traineeToTraineeDTO(trainee)).thenReturn(new TraineeDTO());
//
//        TraineeDTO traineeDTO = traineeFacadeService.getTraineeByUserUsername_(username);
//
//        assertEquals(TraineeDTO.class, traineeDTO.getClass());
//    }
//
//    @Test
//    void updateTrainee_SuccessfulUpdate_ReturnsUpdatedTraineeDTO() {
//        Trainee trainee = new Trainee();
//        when(traineeFacadeService.updateTrainee(trainee)).thenReturn(trainee);
//        when(traineeMapper.traineeToTraineeDTO(trainee)).thenReturn(new TraineeDTO());
//
//        TraineeDTO updatedTraineeDTO = traineeFacadeService.updateTrainee_(trainee);
//
//        assertEquals(TraineeDTO.class, updatedTraineeDTO.getClass());
//    }
//
//    @Test
//    void getTraineeByUserUsernameWithTrainingParams_ValidParams_ReturnsTrainingDTOList() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("userName", "testUsername");
//        Trainee trainee = new Trainee();
//        when(traineeFacadeService.getTraineeByUserUsernameWithTrainingParams(params)).thenReturn(trainee);
//        when(traineeMapper.traineeToTraineeDTOWithTrainings(trainee)).thenReturn(new TraineeDTO());
//        TraineeDTO traineeDTO = new TraineeDTO();
//        when(traineeDTO.getTrainingList()).thenReturn(List.of(new TrainingDTO()));
//
//        List<TrainingDTO> trainingDTOList = traineeFacadeService.getTraineeByUserUsernameWithTrainingParams_(params);
//
//        assertEquals(1, trainingDTOList.size());
//        assertEquals(TrainingDTO.class, trainingDTOList.get(0).getClass());
//    }
//}