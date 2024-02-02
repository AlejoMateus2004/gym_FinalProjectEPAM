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

//    @Mappings({
//            @Mapping(target = "trainerList", ignore = true),
//            @Mapping(target = "trainingList", ignore = true)
//    })
//    Trainee traineeDTOToTrainee(TraineeDTO traineeDTO);
//
//    @Mappings({
//            @Mapping(target = "trainerList", qualifiedBy = RemoveTrainerLists.class),
//            @Mapping(target = "trainingList", ignore = true)
//    })
//    TraineeDTO traineeToTraineeDTO(Trainee trainee);
//
//
//    @Mappings({
//            @Mapping(target = "trainerList", ignore = true),
//            @Mapping(target = "trainingList", qualifiedBy = RemoveTrainingLists.class)
//    })
//    TraineeDTO traineeToTraineeDTOWithTrainings(Trainee trainee);

//    @Mapping(target = "trainingList", ignore = true)
//    @Mapping(target = "traineeList", ignore = true)
//    TrainerDTO trainerToTrainerDTO(Trainer trainer);
//
//    @Mapping(target = "trainee", ignore = true)
//    @Mapping(target = "trainer", qualifiedByName = "mapTrainerToTrainerDTO")
//    TrainingDTO trainingToTrainingDTO(Training training);
//
//    @Named("mapTrainerToTrainerDTO")
//    static TrainerDTO mapTrainerToTrainerDTO(Trainer trainer) {
//        if (trainer == null) {
//            return null;
//        }
//        TrainerDTO trainerDTO = new TrainerDTO();
//        UserDTO userDTO = new UserDTO();
//        userDTO.setFirstName(trainer.getUser().getFirstName());
//        userDTO.setLastName(trainer.getUser().getLastName());
//
//        trainerDTO.setUser(userDTO);
//        return trainerDTO;
//    }
//
//    @RemoveTrainerLists
//    Set<TrainerDTO> mapTrainers(Set<Trainer> trainers);
//
//    @RemoveTrainingLists
//    List<TrainingDTO> mapTrainings(List<Training> trainingList);
//
//
//    @Qualifier
//    @Retention(RetentionPolicy.CLASS)
//    @Target(ElementType.METHOD)
//    @interface RemoveTrainerLists {
//    }
//
//    @Qualifier
//    @Retention(RetentionPolicy.CLASS)
//    @Target(ElementType.METHOD)
//    @interface RemoveTrainingLists {
//    }

}
