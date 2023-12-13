package com.gymepam.DOMAIN;

import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Data
@Entity
@Table(name = "Training")
public class Training implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "traineeId", nullable = false)
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainerId", nullable = false)
    private Trainer trainer;

    @Column(name = "trainingName", nullable = false)
    private String trainingName;

    @ManyToOne
    @JoinColumn(name = "trainingTypeId", nullable = false)
    private Training_Type trainingType;

    @Column(name = "trainingDate", nullable = false)
    private LocalDate trainingDate;

    @Column(name = "trainingDuration", nullable = false)
    private Number trainingDuration;
}
