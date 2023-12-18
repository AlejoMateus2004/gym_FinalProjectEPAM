package com.gymepam.DOMAIN;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Trainee2Trainer")
public class Trainee2Trainer implements Serializable {
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

}
