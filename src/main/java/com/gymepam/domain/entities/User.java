package com.gymepam.domain.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "userName", unique = true, nullable = false )
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

}
