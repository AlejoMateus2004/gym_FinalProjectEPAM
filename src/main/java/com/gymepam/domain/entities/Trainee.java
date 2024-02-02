package com.gymepam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "Trainee")
public class Trainee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "traineeId", nullable = false)
    private Long traineeId;

    @Valid
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, unique = true)
    private User user;

    @Column(name = "dateOfBirth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = true)
    private String address;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Training> trainingList;


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "trainee2trainer",
            joinColumns = @JoinColumn(name = "traineeId"),
            inverseJoinColumns = @JoinColumn(name = "trainerId"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"traineeId", "trainerId"})
    )
    private Set<Trainer> trainerList;
}
