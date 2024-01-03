package com.gymepam.config;

import com.gymepam.dao.*;
import com.gymepam.dao.INMEMORY.*;
import com.gymepam.service.util.validatePassword;
import com.gymepam.service.util.validatePasswordImpl;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.gymepam")
public class Config {

    @Bean
    @Primary
    @Profile("jpa")
    public TraineeRepo jpaTraineeRepoBean(TraineeRepository traineeRepository) {
        return traineeRepository;
    }

    @Bean
    @Primary
    @Profile("inMemory")
    public TraineeRepo inMemoryTraineeRepoBean(TraineeStorageInMemory traineeStorageInMemory) {
        return traineeStorageInMemory;
    }

    @Bean
    @Primary
    @Profile("jpa")
    public TrainerRepo jpaTrainerRepoBean(TrainerRepository trainerRepository) {
        return trainerRepository;
    }

    @Bean
    @Primary
    @Profile("inMemory")
    public TrainerRepo inMemoryTrainerRepoBean(TrainerStorageInMemory trainerStorageInMemory) {
        return trainerStorageInMemory;
    }

    @Bean
    @Primary
    @Profile("jpa")
    public TrainingRepo jpaTrainingRepoBean(TrainingRepository trainingRepository) {
        return trainingRepository;
    }

    @Bean
    @Primary
    @Profile("inMemory")
    public TrainingRepo inMemoryTrainingRepoBean(TrainingStorageInMemory trainingStorageInMemory) {
        return trainingStorageInMemory;
    }

    @Bean
    @Primary
    @Profile("jpa")
    public TrainingTypeRepo jpaTrainingTypeRepoBean(TrainingTypeRepository trainingTypeRepository) {
        return trainingTypeRepository;
    }

    @Bean
    @Primary
    @Profile("inMemory")
    public TrainingTypeRepo inMemoryTrainingTypeRepoBean(TrainingTypeStorageInMemory trainingTypeStorageInMemory) {
        return trainingTypeStorageInMemory;
    }

    @Bean
    @Primary
    @Profile("jpa")
    public UserRepo jpaUserRepoBean(UserRepository userRepository) {
        return userRepository;
    }

    @Bean
    @Primary
    @Profile("inMemory")
    public UserRepo inMemoryUserRepoBean(UserStorageInMemory userStorageInMemory) {
        return userStorageInMemory;
    }

    @Bean
    @Primary
    public validatePassword validatePassword(validatePasswordImpl validatePasswordImpl){
        return validatePasswordImpl;
    }
}
