package com.gymepam.config;

import com.gymepam.dao.*;
import com.gymepam.dao.inmemory.TraineeStorageInMemory;
import com.gymepam.dao.inmemory.TrainerStorageInMemory;
import com.gymepam.dao.inmemory.TrainingTypeStorageInMemory;
import com.gymepam.dao.inmemory.UserStorageInMemory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackages = "com.gymepam")
public class RepoConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TraineeRepo jpaTraineeRepoBean(TraineeRepository traineeRepository) {
        return traineeRepository;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TrainerRepo jpaTrainerRepoBean(TrainerRepository trainerRepository) {
        return trainerRepository;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public TrainingTypeRepo jpaTrainingTypeRepoBean(TrainingTypeRepository trainingTypeRepository) {
        return trainingTypeRepository;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa")
    public UserRepo jpaUserRepoBean(UserRepository userRepository) {
        return userRepository;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TraineeRepo inMemoryTraineeRepoBean() {
        return new TraineeStorageInMemory();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TrainerRepo inMemoryTrainerRepoBean() {
        return new TrainerStorageInMemory();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public TrainingTypeRepo inMemoryTrainingTypeRepoBean() {
        return new TrainingTypeStorageInMemory();
    }


    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.repository", havingValue = "in-memory")
    public UserRepo inMemoryUserRepoBean( ) {
        return new UserStorageInMemory();
    }


}
