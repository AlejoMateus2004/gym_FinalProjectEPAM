package com.gymepam.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppProperties {

    @Value("${initializationStorageFile.path}")
    private String excelFilePath;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${initializationStorageFile.active}")
    private boolean initializeStorage;

}
