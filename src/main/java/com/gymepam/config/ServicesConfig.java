package com.gymepam.config;

import com.gymepam.service.messaging.ActiveMqProducer;
import com.gymepam.service.messaging.AwsSqsProducer;
import com.gymepam.service.messaging.Producer;
import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.training.TrainingServiceFeignImpl;
import com.gymepam.service.training.TrainingServiceSqsImpl;
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
    @ConditionalOnProperty(name = "microservice.connection", havingValue = "feign")
    public TrainingMicroService trainingMicroServiceFeign(){
        return new TrainingServiceFeignImpl();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "microservice.connection", havingValue = "activemq")
    public TrainingMicroService trainingMicroServiceActiveMq(){
        return new TrainingServiceSqsImpl();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "microservice.connection", havingValue = "aws")
    public TrainingMicroService trainingMicroServiceAwsSqs(){
        return new TrainingServiceSqsImpl();
    }
    @Bean
    @Primary
    @ConditionalOnProperty(name = "microservice.connection", havingValue = "activemq")
    public Producer producerActiveMq(){
        return new ActiveMqProducer();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "microservice.connection", havingValue = "aws")
    public Producer producerAwsSqs(){
        return new AwsSqsProducer();
    }

}
