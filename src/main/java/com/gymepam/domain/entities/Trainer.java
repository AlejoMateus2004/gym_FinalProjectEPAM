package com.gymepam.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "Trainer")
public class Trainer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trainerId", nullable = false)
    private Long trainerId;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "specializationId")
    private TrainingType trainingType;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "trainerList", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Trainee> traineeList;
}
