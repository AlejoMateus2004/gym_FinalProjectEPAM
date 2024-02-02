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

//    @Mappings({
//            @Mapping(target = "traineeList", qualifiedBy = RemoveTraineeLists.class),
//            @Mapping(target = "trainingList", ignore = true)
//    })
//    TrainerDTO trainerToTrainerDTO(Trainer trainer);
//
//    @Mappings({
//            @Mapping(target = "traineeList", ignore = true),
//            @Mapping(target = "trainingList", qualifiedBy = TrainerMapper.RemoveTrainingLists.class)
//    })
//    TrainerDTO trainerToTrainerDTOWithTrainings(Trainer trainer);
//
//    @Mapping(target = "trainer", ignore = true)
//    @Mapping(target = "trainee", qualifiedByName = "mapTraineeToTraineeDTO")
//    TrainingDTO trainingToTrainingDTO(Training training);
//
//    @Mapping(target = "trainingList", ignore = true)
//    @Mapping(target = "trainerList", ignore = true)
//    TraineeDTO traineeToTraineeDTO(Trainee trainee);
//    @Named("mapTraineeToTraineeDTO")
//    static TraineeDTO mapTraineeToTraineeDTO(Trainee trainee) {
//        if (trainee == null) {
//            return null;
//        }
//        TraineeDTO traineeDTO = new TraineeDTO();
//        UserDTO userDTO = new UserDTO();
//        userDTO.setFirstName(trainee.getUser().getFirstName());
//        userDTO.setLastName(trainee.getUser().getLastName());
//
//        traineeDTO.setUser(userDTO);
//        return traineeDTO;
//    }
//
//
//    @RemoveTraineeLists
//    Set<TraineeDTO> mapTrainees(Set<Trainee> trainees);
//
//    @RemoveTrainingLists
//    List<TrainingDTO> mapTrainings(List<Training> trainingList);



//    @Qualifier
//    @Retention(RetentionPolicy.CLASS)
//    @Target(ElementType.METHOD)
//    @interface RemoveTraineeLists {
//    }
//
//    @Qualifier
//    @Retention(RetentionPolicy.CLASS)
//    @Target(ElementType.METHOD)
//    @interface RemoveTrainingLists {
//    }

}
