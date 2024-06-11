package com.gymepam.web.controllers;

import com.gymepam.config.GlobalModelResponse;
import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.training.facade.TrainingFacadeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class TrainingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingFacadeService trainingFacadeService;

    @Test
    void saveTraining() throws Exception {
        String requestBody = "{\n" +
                "  \"traineeUsername\": \"traineeUsername\",\n" +
                "  \"trainerUsername\": \"trainerUsername\",\n" +
                "  \"trainingName\": \"TrainingName\",\n" +
                "  \"trainingDate\": \"" + LocalDate.now() + "\",\n" +
                "  \"trainingDuration\": 60\n" +
                "}";

        GlobalModelResponse response = new GlobalModelResponse();
        response.setResponse("Training Saved");

        when(trainingFacadeService.saveTraining(any(TrainingRecord.TrainingRequest.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        mockMvc.perform(post("/training")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}