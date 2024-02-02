package com.gymepam.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Data
public class AppProperties {

    @Value("${initializationStorageFile.path}")
    private String excelFilePath;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${initializationStorageFile.active}")
    private boolean initializeStorage;

}
