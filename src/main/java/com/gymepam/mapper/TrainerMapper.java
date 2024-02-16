package com.gymepam.mapper;

import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.entities.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {


    Trainer trainerRequestToTrainer(TrainerRecord.TrainerRequest traineeRequest);

    TrainerRecord.TrainerResponseWithTrainees trainerToTrainerResponseWithTrainees(Trainer trainer);

    Trainer trainerUpdateRequestToTrainer(TrainerRecord.TrainerUpdateRequest trainerRequest);
    TrainerRecord.TrainerResponseWithTrainings trainerToTrainerResponseWithTrainings(Trainer trainer);

    TrainerRecord.TrainerResponse trainerToTrainerResponse(Trainer t);

}
