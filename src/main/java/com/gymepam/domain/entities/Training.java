package com.gymepam.domain.entities;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class Training implements Serializable {

    private Long Id;
    private Trainee trainee;
    private Trainer trainer;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private Long trainingDuration;
}
