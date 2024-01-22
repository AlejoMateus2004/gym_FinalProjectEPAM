package com.gymepam.config;

import com.gymepam.dao.*;
import com.gymepam.dao.INMEMORY.*;
import com.gymepam.service.util.ValidatePassword;
import com.gymepam.service.util.ValidatePasswordBCryptImpl;
import com.gymepam.service.util.ValidatePasswordImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "com.gymepam")
public class ServicesConfig {

    @Value("${validate.encoded.enabled}")
    private boolean bcryptEnabled;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if (bcryptEnabled) {
            return new BCryptPasswordEncoder();
        } else {
            return NoOpPasswordEncoder.getInstance();
        }
    }

    @Bean
    @DependsOn("passwordEncoder")
    @Primary
    @ConditionalOnProperty(name = "validate.password", havingValue = "bcrypt")
    public ValidatePassword validatePasswordWithBCrypt(ValidatePasswordBCryptImpl validatePasswordBCryptImpl){
        return validatePasswordBCryptImpl;
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "validate.password", havingValue = "nobcrypt")
    public ValidatePassword validatePassword(ValidatePasswordImpl validatePasswordImpl){
        return validatePasswordImpl;
    }

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



}
