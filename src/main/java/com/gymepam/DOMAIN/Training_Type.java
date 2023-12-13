package com.gymepam.DOMAIN;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Training_Type")
public class Training_Type implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @Column(name = "trainingTypeName", nullable = false)
    private String trainingTypeName;
}
