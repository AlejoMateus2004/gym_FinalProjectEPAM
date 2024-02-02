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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TraineeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TraineeFacadeService traineeFacadeService;

    @Test
    void saveTrainee() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");

        String requestBody = "{\n" +
                "  \"address\": \"Cra 13 #1-33\",\n" +
                "  \"dateOfBirth\": \"2024-02-02\",\n" +
                "  \"user\": {\n" +
                "    \"firstName\": \"Alejandro\",\n" +
                "    \"lastName\": \"Mateus\"\n" +
                "  }\n" +
                "}";

        Mockito.when(traineeFacadeService.save_Trainee(Mockito.any(TraineeRecord.TraineeRequest.class)))
                .thenReturn(ResponseEntity.ok(authenticationRequest));

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
                        , "Cra 13 # 1-33"
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
                .andExpect(jsonPath("$.user.username").value("alejandro.mateus"));
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
                , "Cra 13 # 1-33"
                , new HashSet<>()
        );

        Mockito.when(traineeFacadeService.getTraineeByUserUsername_("alejandro.mateus"))
                .thenReturn(traineeResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainee/username")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username").value("alejandro.mateus"))
                .andExpect(jsonPath("$.address").value("2024-02-02"));
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
                        .content("{\"trainee_username\":\"username\",\"trainer_usernames\":[\"alejandro.mateus\"]}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].firstName").value("Alejandro"))
                .andExpect(jsonPath("$[0].lastName").value("Mateus"))
                .andExpect(jsonPath("$[0].userName").value("alejandro.mateus"));
    }

    @Test
    void getTraineeTrainingsList() throws Exception {
        LocalDate periodFrom = LocalDate.parse("2024-01-01");
        LocalDate periodTo = LocalDate.now();
        String username = "alejandro.mateus";

        TraineeRecord.TraineeRequestWithTrainingParams traineeRequest =
                new TraineeRecord.TraineeRequestWithTrainingParams(username, new TrainingRecord.TrainingFilterRequest(periodFrom, periodTo, "alejandro.mateus", null));

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
                        .param("periodFrom", periodFrom.toString())
                        .param("periodTo", periodTo.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).
                andExpect(jsonPath("$.trainingName").value("1st month"));

    }
}
               
