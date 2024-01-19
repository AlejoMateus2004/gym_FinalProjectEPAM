package com.gymepam.config;

import com.gymepam.dao.*;
import com.gymepam.dao.inmemory.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "com.gymepam")
public class RepoConfig {

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TraineeRepo jpaTraineeRepoBean(TraineeRepository traineeRepository) {
        return traineeRepository;
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TrainerRepo jpaTrainerRepoBean(TrainerRepository trainerRepository) {
        return trainerRepository;
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TrainingRepo jpaTrainingRepoBean(TrainingRepository trainingRepository) {
        return trainingRepository;
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TrainingTypeRepo jpaTrainingTypeRepoBean(TrainingTypeRepository trainingTypeRepository) {
        return trainingTypeRepository;
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public UserRepo jpaUserRepoBean(UserRepository userRepository) {
        return userRepository;
    }

//    @Bean
//    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
//    public TraineeRepo inMemoryTraineeRepoBean() {
//        return new TraineeStorageInMemory();
//    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TrainerRepo inMemoryTrainerRepoBean() {
        return new TrainerStorageInMemory();
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TrainingRepo inMemoryTrainingRepoBean() {
        return new TrainingStorageInMemory();
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TrainingTypeRepo inMemoryTrainingTypeRepoBean() {
        return new TrainingTypeStorageInMemory();
    }


    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public UserRepo inMemoryUserRepoBean( ) {
        return new UserStorageInMemory();
    }


}
