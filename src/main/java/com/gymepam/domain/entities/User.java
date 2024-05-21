package com.gymepam.domain.entities;

import lombok.Data;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "`User`")
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

    @Column(name = "failedLoginAttempts")
    private Long failedLoginAttempts;

    @Column(name = "unlockDateTime")
    private LocalDateTime unlockDateTime;


    public boolean verifyIsLockedTemporarily(){

        if (unlockDateTime != null && LocalDateTime.now().isAfter(unlockDateTime)) {
//            isActive = true;
            restartSessionValues();
            return false;
        }else if (unlockDateTime != null && LocalDateTime.now().isBefore(unlockDateTime)) {
            return true;
        }
        return false;
    }
    private void assignLockTime() {
        if (unlockDateTime == null) {
            unlockDateTime = LocalDateTime.now().plusMinutes(5);
        }
    }

    private void changeStatusByLoginAttempts() {
//        isActive = (failedLoginAttempts == 3) ? false : true;
        if (failedLoginAttempts == 3) {
            assignLockTime();
        }
    }
    public void incrementFailedLoginAttempts() {
        failedLoginAttempts = (failedLoginAttempts == null) ? 1 : failedLoginAttempts+1;
        changeStatusByLoginAttempts();
    }

    private void restartSessionValues() {
        failedLoginAttempts = null;
        unlockDateTime = null;
    }


}
