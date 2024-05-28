package com.gymepam.domain.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "TrainingType")
public class TrainingType implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @Column(name = "trainingTypeName")
    private String trainingTypeName;
}
