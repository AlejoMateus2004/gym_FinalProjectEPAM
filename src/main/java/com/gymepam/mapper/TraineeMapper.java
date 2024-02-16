package com.gymepam.mapper;

import com.gymepam.domain.dto.records.TraineeRecord;
import com.gymepam.domain.entities.Trainee;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraineeMapper {

    Trainee traineeRequestToTrainee(TraineeRecord.TraineeRequest traineeRequest);

    Trainee traineeUpdateRequestToTrainee(TraineeRecord.TraineeUpdateRequest traineeRequest);

    TraineeRecord.TraineeResponseWithTrainers traineeToTraineeResponseWithTrainers(Trainee trainee);

    TraineeRecord.TraineeResponseWithTrainings traineeToTraineeResponseWithTrainings(Trainee trainee);

}
