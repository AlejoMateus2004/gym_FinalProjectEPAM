package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.dto.records.UserRecord;
import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.facade.TrainerFacadeService;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class TrainerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainerFacadeService trainerFacadeService;

    @Test
    void saveTrainer() throws Exception {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");

        String requestBody = "{\n" +
                "  \"user\": {\n" +
                "    \"firstName\": \"Alejandro\",\n" +
                "    \"lastName\": \"Mateus\"\n" +
                "  },\n" +
                "  \"trainingType\": {\n" +
                "    \"id\": 1\n" +
                "  }\n" +
                "}";

        Mockito.when(trainerFacadeService.save_Trainer(Mockito.any(TrainerRecord.TrainerRequest.class)))
                .thenReturn(ResponseEntity.ok(authenticationRequest));

        mockMvc.perform(MockMvcRequestBuilders.post("/trainer/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.password").value("password"));
    }

    @Test
    void updateTrainer() throws Exception {
        Mockito.when(trainerFacadeService.updateTrainer_(Mockito.any(TrainerRecord.TrainerUpdateRequest.class)))
                .thenReturn(new TrainerRecord.TrainerResponseWithTrainees(
                        new UserRecord.UserComplete(
                                "Alejandro A",
                                "Mateus",
                                true,
                                "alejandro.mateus")
                        , null
                        , new HashSet<>()

                ));

        String requestBody = "{\n" +
                "  \"user\": {\n" +
                "    \"firstName\": \"Alejandro A\",\n" +
                "    \"lastName\": \"Mateus\",\n" +
                "    \"userName\": \"alejandro.mateus\",\n" +
                "    \"isActive\": true\n" +
                "  },\n" +
                "  \"trainingType\": {\n" +
                "    \"id\": 1\n" +
                "  }\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.put("/trainer/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.user.firstName").value("Alejandro A"))
                .andExpect(jsonPath("$.user.userName").value("alejandro.mateus"));
    }

    @Test
    void getTrainer() throws Exception{
        TrainerRecord.TrainerResponseWithTrainees trainerResponse = new TrainerRecord.TrainerResponseWithTrainees(
                new UserRecord.UserComplete(
                        "Alejandro",
                        "Mateus",
                        true,
                        "alejandro.mateus")
                , new TrainingType()
                , new HashSet<>()
        );

        Mockito.when(trainerFacadeService.getTrainerByUserUsername_("alejandro.mateus"))
                .thenReturn(trainerResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainer/alejandro.mateus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.userName").value("alejandro.mateus"));
    }

    @Test
    void getTrainerByTrainingParams() throws Exception{
        LocalDate periodFrom = LocalDate.parse("2024-01-01");
        LocalDate periodTo = LocalDate.now();
        String username = "alejandro.mateus";

        TrainerRecord.TrainerRequestWithTrainingParams trainerRequest =
                new TrainerRecord.TrainerRequestWithTrainingParams(
                        username,
                        new TrainingRecord.TrainingFilterRequest(
                                periodFrom,
                                periodTo,
                                "",
                                null));

        List<TrainingRecord.TrainerTrainingResponse> trainingsResponse = Collections.singletonList(
                new TrainingRecord.TrainerTrainingResponse(
                        "1st month",
                        LocalDate.parse("2024-01-01"),
                        new TraineeRecord.TraineeUserResponse(
                                new UserRecord.UserRequest(
                                        "Alejandro",
                                        "Mateus"
                                )
                        ),
                        null
                )
        );

        Mockito.when(trainerFacadeService.getTrainerByUserUsernameWithTrainingParams_(trainerRequest))
                .thenReturn(trainingsResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/trainer/trainings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username", username)
                        .param("periodFrom", String.valueOf(periodFrom))
                        .param("periodTo", String.valueOf(periodTo))
                        .param("traineeName","")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trainingName").value("1st month"));

    }


    @Test
    void testUpdateStatusSuccess() throws Exception {
        String username = "testUsername";
        boolean isActive = true;

        mockMvc.perform(patch("/trainer")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(trainerFacadeService, times(1)).updateStatus(username, isActive);
    }

    @Test
    void testUpdateStatusFailure() throws Exception {
        String username = "testUsername";
        boolean isActive = true;

        doThrow(new RuntimeException("Simulated exception")).when(trainerFacadeService).updateStatus(username, isActive);

        mockMvc.perform(patch("/trainer")
                        .param("username", username)
                        .param("isActive", String.valueOf(isActive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        verify(trainerFacadeService, times(1)).updateStatus(username, isActive);
    }
}