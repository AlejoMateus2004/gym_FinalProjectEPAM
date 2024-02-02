package com.gymepam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
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
    @JoinColumn(name = "specializationId", nullable = false)
    private TrainingType trainingType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Training> trainingList;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "trainerList", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Trainee> traineeList;
}
