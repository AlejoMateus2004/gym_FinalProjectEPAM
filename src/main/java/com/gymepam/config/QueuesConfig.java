package com.gymepam.config;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "queues")
public class QueuesConfig {

    private String saveTraining;
    private String updateTraining;
    private String summaryTrainer;
    private String deleteTraining;
    private String trainerTrainingList;
    private String traineeTrainingList;
    private @Nullable String responses;
}
