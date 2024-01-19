package com.gymepam.config;

import com.gymepam.service.util.ValidatePassword;
import com.gymepam.service.util.ValidatePasswordBCryptImpl;
import com.gymepam.service.util.ValidatePasswordImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "com.gymepam")
public class ServicesConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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

}
