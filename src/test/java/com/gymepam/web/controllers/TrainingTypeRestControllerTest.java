package com.gymepam.web.controllers;

import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.trainingtype.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(SpringExtension.class)
class TrainingTypeRestControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingTypeService trainingTypeService;

    @Test
    void getTrainingTypeList() throws Exception {

        TrainingType trainingType1 = new TrainingType();
        trainingType1.setId(1L);
        trainingType1.setTrainingTypeName("Type 1");
        TrainingType trainingType2 = new TrainingType();
        trainingType2.setId(2L);
        trainingType2.setTrainingTypeName("Type 2");

        List<TrainingType> trainingTypes = Arrays.asList(trainingType1, trainingType2);

        when(trainingTypeService.getAllTrainingTypes()).thenReturn(trainingTypes);

        mockMvc.perform(MockMvcRequestBuilders.get("/training-type/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].trainingTypeName").value("Type 1"))
                .andExpect(jsonPath("$[1].trainingTypeName").value("Type 2"));


    }
}