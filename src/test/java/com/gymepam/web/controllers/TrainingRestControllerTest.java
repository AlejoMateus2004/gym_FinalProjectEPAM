package com.gymepam.web.controllers;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.service.facade.TrainingFacadeService;
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
import org.springframework.test.web.servlet.MvcResult;

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
    private TrainingFacadeService trainingFacade;
    @Test
    void saveTraining() throws Exception {
        String requestBody = "{\n" +
                "  \"traineeUsername\": \"traineeUsername\",\n" +
                "  \"trainerUsername\": \"trainerUsername\",\n" +
                "  \"trainingName\": \"TrainingName\",\n" +
                "  \"trainingDate\": \"" + LocalDate.now() + "\",\n" +
                "  \"trainingDuration\": 60\n" +
                "}";

        when(trainingFacade.saveTraining_(any(TrainingRecord.TrainingRequest.class)))
                .thenReturn(new ResponseEntity("Training saved successfully", HttpStatus.CREATED));

        MvcResult result = mockMvc.perform(post("/training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andReturn();

        verify(trainingFacade, times(1)).saveTraining_(any(TrainingRecord.TrainingRequest.class));
    }
}