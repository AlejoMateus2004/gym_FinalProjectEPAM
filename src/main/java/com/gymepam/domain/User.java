package com.gymepam.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Entity
@Table(name = "User")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", unique = true, nullable = false)
    private Long Id;

    @Column(name = "firstName", nullable = false)
    @NotBlank(message = "Firstname can't be null or empty")
    private String firstName;

    @Column(name = "lastName", nullable = false)
    @NotBlank(message = "Lastname can't be null or empty")
    private String lastName;

    @Column(name = "userName", unique = true, nullable = false )
    @NotBlank(message = "Username can't be null or empty")
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password can't be null or empty")
    private String password;

    @Column(name = "isActive", nullable = false)
    private Boolean isActive;

}
