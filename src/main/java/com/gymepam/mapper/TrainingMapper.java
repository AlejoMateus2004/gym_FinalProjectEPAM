package com.gymepam.mapper;

import com.gymepam.domain.dto.records.TrainingRecord;
import com.gymepam.domain.entities.Training;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
//    Training trainingRequestToTraining(TrainingRecord.TrainingRequest trainingRequest);

    @Mapping(source = "trainer.user.userName", target = "trainerUsername")
    @Mapping(source = "trainee.user.userName", target = "traineeUsername")
    TrainingRecord.TrainingRequest trainingToTrainingRequest(Training training);
}
