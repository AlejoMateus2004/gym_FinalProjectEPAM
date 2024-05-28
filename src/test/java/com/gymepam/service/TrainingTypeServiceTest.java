package com.gymepam.service;

import com.gymepam.dao.TrainingTypeRepo;
import com.gymepam.domain.entities.TrainingType;
import com.gymepam.service.trainingtype.TrainingTypeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeServiceTest {

    @Mock
    private TrainingTypeRepo trainingTypeRepository;

    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @DisplayName("Test save TrainingType, Successfully Saved")
    @Test
    void saveTraining_Type_SuccessfullySaved() {
        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.save(trainingType)).thenReturn(trainingType);

        TrainingType resultTrainingType = trainingTypeService.saveTraining_Type(trainingType);

        assertEquals(trainingType, resultTrainingType);
        verify(trainingTypeRepository, times(1)).save(trainingType);
    }

    @DisplayName("Test save TrainingType, and result is null")
    @Test
    void saveTraining_Type_ExceptionThrown_LogsErrorAndReturnsNull() {
        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.save(trainingType)).thenThrow(new RuntimeException("Error while saving"));

        TrainingType resultTrainingType = trainingTypeService.saveTraining_Type(trainingType);

        assertNull(resultTrainingType);
    }

    @DisplayName("Test get TrainingType")
    @Test
    void getTraining_Type_ExistingId_ReturnsTrainingType() {
        Long trainingTypeId = 1L;
        TrainingType trainingType = new TrainingType();
        when(trainingTypeRepository.findById(trainingTypeId)).thenReturn(java.util.Optional.of(trainingType));

        TrainingType resultTrainingType = trainingTypeService.getTraining_Type(trainingTypeId);

        assertEquals(trainingType, resultTrainingType);
    }


    @DisplayName("Test get all TrainingTypes")
    @Test
    void getAllTrainingTypes_ReturnsTrainingTypesList() {
        List<TrainingType> trainingTypes = new ArrayList<>();
        when(trainingTypeRepository.findAll()).thenReturn(trainingTypes);

        List<TrainingType> resultTrainingTypes = trainingTypeService.getAllTrainingTypes();

        assertEquals(trainingTypes, resultTrainingTypes);
    }
}