package com.gymepam.domain;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "Trainee2Trainer", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"traineeId", "trainerId"})
})
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

    @Override
    public String toString() {
        return "Trainee2Trainer{" +
                "Id=" + Id +
                ", trainee=" + trainee.getUser().getUserName() +
                ", trainer=" + trainer.getUser().getUserName() +
                '}';
    }
}
