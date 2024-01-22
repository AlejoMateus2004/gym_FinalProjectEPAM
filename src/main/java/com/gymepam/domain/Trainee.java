package com.gymepam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Trainee")
public class Trainee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long Id;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false, unique = true)
    @Valid
    private User user;

    @Column(name = "dateOfBirth", nullable = true)
    private LocalDate dateOfBirth;

    @Column(name = "address", nullable = true)
    private String address;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "trainee", fetch = FetchType.LAZY)
    private List<Trainee2Trainer> trainerList;

}
