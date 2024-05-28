package com.gymepam.config;

import com.gymepam.service.training.TrainingMicroService;
import com.gymepam.service.training.TrainingServiceActiveMqImpl;
import com.gymepam.service.training.TrainingServiceFeignImpl;
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
        return new TrainingServiceActiveMqImpl();
    }

}
