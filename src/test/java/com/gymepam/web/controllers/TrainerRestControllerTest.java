package com.gymepam.web.controllers;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.dto.records.UserRecord;
import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.trainer.facade.TrainerFacadeService;
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

import java.util.HashSet;

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

        String requestBody = """
                {
                  "user": {
                    "firstName": "Alejandro",
                    "lastName": "Mateus"
                  },
                  "trainingType": {
                    "id": 1
                  }
                }""";

        Mockito.when(trainerFacadeService.save_Trainer(Mockito.any(TrainerRecord.TrainerRequest.class)))
                .thenReturn(ResponseEntity.ok(authenticationRequest));

        mockMvc.perform(MockMvcRequestBuilders.post("/trainer/public/save")
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

        String requestBody = """
                {
                  "user": {
                    "firstName": "Alejandro A",
                    "lastName": "Mateus",
                    "userName": "alejandro.mateus",
                    "isActive": true
                  },
                  "trainingType": {
                    "id": 1
                  }
                }""";

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

//    @Test
//    void getTraineeByTrainingParams() throws Exception {
//        LocalDate periodFrom = LocalDate.parse("2024-01-01");
//        LocalDate periodTo = LocalDate.now();
//        String trainerUsername = "trainer.username";
//        String traineeUsername = "trainee.username";
//
//        TrainingRecord.TrainerTrainingParamsRequest trainerRequest = new TrainingRecord.TrainerTrainingParamsRequest(
//                periodFrom,
//                periodTo,
//                trainerUsername,
//                traineeUsername
//        );
//
//        List<TrainingRecord.TrainerTrainingResponse> trainingsResponse = Collections.singletonList(
//                new TrainingRecord.TrainerTrainingResponse(
//                        1L,
//                        "TRAINING1",
//                        LocalDate.parse("2024-02-01"),
//                        trainerUsername
//                )
//        );
//
//        Mockito.when(trainerFacadeService.getTrainerByUserUsernameWithTrainingParams(trainerRequest))
//                .thenReturn(new ResponseEntity<>(trainingsResponse, HttpStatus.OK));
//        mockMvc.perform(MockMvcRequestBuilders.post("/trainer/trainings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\n" +
//                                "  \"periodFrom\": \"2024-01-01\",\n" +
//                                "  \"periodTo\": \"2024-05-11\",\n" +
//                                "  \"trainerUsername\": \"trainer.username\",\n" +
//                                "  \"traineeUsername\": \"trainee.username\"\n" +
//                                "}")
//                        .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("$[0].id").value(1))
//                        .andExpect(jsonPath("$[0].trainingName").value("TRAINING1"))
//                        .andExpect(jsonPath("$[0].trainingDate").value("2024-02-01"))
//                        .andExpect(jsonPath("$[0].traineeUsername").value(traineeUsername));
//    }


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