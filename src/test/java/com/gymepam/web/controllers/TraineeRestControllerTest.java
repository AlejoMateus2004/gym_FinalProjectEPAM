package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.UserRecord;
import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.facade.TraineeFacadeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class TraineeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeFacadeService traineeFacadeService;

    @Test
    void saveTrainee() throws Exception {
        AuthenticationRequest authenticationResponse= new AuthenticationRequest();
        authenticationResponse.setUsername("username");
        authenticationResponse.setPassword("password");

        String requestBody = "{\n" +
                "  \"address\": \"Cra 13 #1-33\",\n" +
                "  \"dateOfBirth\": \"2024-02-02\",\n" +
                "  \"user\": {\n" +
                "    \"firstName\": \"Alejandro\",\n" +
                "    \"lastName\": \"Mateus\"\n" +
                "  }\n" +
                "}";

        Mockito.when(traineeFacadeService.save_Trainee(Mockito.any(TraineeRecord.TraineeRequest.class)))
                .thenReturn(ResponseEntity.ok(authenticationResponse));

        mockMvc.perform(MockMvcRequestBuilders.post("/trainee/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void updateTrainee() throws Exception {

        Mockito.when(traineeFacadeService.updateTrainee_(Mockito.any(TraineeRecord.TraineeUpdateRequest.class)))
                .thenReturn(new TraineeRecord.TraineeResponseWithTrainers(
                        new UserRecord.UserComplete(
                                "Alejandro",
                                "Mateus",
                                true,
                                "alejandro.mateus")
                        , LocalDate.parse("2024-02-02")
                        , "Cra 13 #1-33"
                        , new HashSet<>()
                ));

        String requestBody = "{\n" +
                "  \"address\": \"Cra 13 #1-33\",\n" +
                "  \"dateOfBirth\": \"2024-02-02\",\n" +
                "  \"user\": {\n" +
                "    \"firstName\": \"Alejandro\",\n" +
                "    \"lastName\": \"Mateus\",\n" +
                "    \"userName\": \"alejandro.mateus\",\n" +
                "    \"isActive\": true\n" +
                "  }\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/trainee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.address").value("Cra 13 #1-33"))
                .andExpect(jsonPath("$.user.userName").value("alejandro.mateus"));
    }

    @Test
    void getTrainee() throws Exception {
        TraineeRecord.TraineeResponseWithTrainers traineeResponse = new TraineeRecord.TraineeResponseWithTrainers(
                new UserRecord.UserComplete(
                        "Alejandro",
                        "Mateus",
                        true,
                        "alejandro.mateus")
                , LocalDate.parse("2024-02-02")
                , "Cra 13 #1-33"
                , new HashSet<>()
        );

        Mockito.when(traineeFacadeService.getTraineeByUserUsername_("alejandro.mateus"))
                .thenReturn(traineeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/alejandro.mateus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName").value("alejandro.mateus"))
                .andExpect(jsonPath("$.dateOfBirth").value("2024-02-02"));
    }

    @Test
    void updateTraineesTrainerList() throws Exception {

        Set<TrainerRecord.TrainerResponse> trainers = new HashSet<>();
        trainers.add(new TrainerRecord.TrainerResponse(
                new UserRecord.UserComplete(
                        "Alejandro",
                        "Mateus",
                        true,
                        "alejandro.mateus")
                , new TrainingType()
                ));

        Mockito.when(traineeFacadeService.updateTraineesTrainerList("username", Collections.singleton("alejandro.mateus")))
                .thenReturn(trainers);

        mockMvc.perform(MockMvcRequestBuilders.put("/trainee/trainers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"trainee_username\":\"username\",\"trainerUsernames\":[\"alejandro.mateus\"]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].user.firstName").value("Alejandro"))
                .andExpect(jsonPath("$[0].user.lastName").value("Mateus"))
                .andExpect(jsonPath("$[0].user.userName").value("alejandro.mateus"));
    }

    @Test
    void getTraineeTrainingsList() throws Exception {
        LocalDate periodFrom = LocalDate.parse("2024-01-01");
        LocalDate periodTo = LocalDate.now();
        String username = "alejandro.mateus";

        TraineeRecord.TraineeRequestWithTrainingParams traineeRequest =
                new TraineeRecord.TraineeRequestWithTrainingParams(username, new TrainingRecord.TrainingFilterRequest(periodFrom, periodTo, "", ""));

        List<TrainingRecord.TraineeTrainingResponse> trainingsResponse = Collections.singletonList(
                new TrainingRecord.TraineeTrainingResponse(
                        "1st month",
                        LocalDate.parse("2024-01-01"),
                        new TrainerRecord.TrainerUserResponse(
                                new UserRecord.UserRequest(
                                        "Alejandro",
                                        "Mateus"
                                )
                        ),
                        null
                )
        );

        Mockito.when(traineeFacadeService.getTraineeByUserUsernameWithTrainingParams_(traineeRequest))
                .thenReturn(trainingsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username)
                        .param("periodFrom", String.valueOf(periodFrom))
                        .param("periodTo", String.valueOf(periodTo))
                        .param("trainerName","")
                        .param("trainingType","")
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].trainingName").value("1st month"));

    }

    @Test
    void deleteTrainee() throws Exception{
        String username = "username";

        mockMvc.perform(delete("/trainee/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(traineeFacadeService, times(1)).deleteByUserUserName(username);
    }

    @Test
    void testDeleteTraineeFailure() throws Exception {
        String username = "username";

        doThrow(new RuntimeException("Simulated exception")).when(traineeFacadeService).deleteByUserUserName(username);

        mockMvc.perform(delete("/trainee/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(traineeFacadeService, times(1)).deleteByUserUserName(username);
    }

    @Test
    void testUpdateStatusSuccess() throws Exception {
        String username = "testUsername";
        boolean isActive = true;

        mockMvc.perform(patch("/trainee")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(traineeFacadeService, times(1)).updateStatus(username, isActive);
    }

    @Test
    void testUpdateStatusFailure() throws Exception {
        String username = "testUsername";
        boolean isActive = true;

        doThrow(new RuntimeException("Simulated exception")).when(traineeFacadeService).updateStatus(username, isActive);

        mockMvc.perform(patch("/trainee")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(traineeFacadeService, times(1)).updateStatus(username, isActive);
    }

    @Test
    void testGetNotAssignedTrainersSuccess() throws Exception {
        String username = "testUsername";

        Set<TrainerRecord.TrainerResponse> trainersNotAssigned = new HashSet<>();

        when(traineeFacadeService.getNotAssignedTrainersByTraineeUserUsername(username)).thenReturn(trainersNotAssigned);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/{username}/trainers-notAssigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(trainersNotAssigned.size()));
    }

    @Test
    void testGetNotAssignedTrainersNotFound() throws Exception {
        String username = "testUsername";

        when(traineeFacadeService.getNotAssignedTrainersByTraineeUserUsername(username)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/{username}/trainers-notAssigned", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
               
