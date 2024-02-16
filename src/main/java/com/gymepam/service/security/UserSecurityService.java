package com.gymepam.service.security;

import com.gymepam.domain.Roles;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import com.gymepam.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Slf4j
@AllArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String role = "";
        com.gymepam.domain.entities.User userEntity = null;

        Trainee trainee = traineeService.getTraineeByUserUsername(username);
        if (trainee != null) {
            userEntity = trainee.getUser();
            role = Roles.TRAINEE.name();
        } else {
            Trainer trainer = trainerService.getTrainerByUserUsername(username);
            if (trainer != null) {
                userEntity = trainer.getUser();
                role = Roles.TRAINER.name();
            }
        }
        if (userEntity == null) {
            log.error("Error loading user by username: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        String userName = userEntity.getUserName();
        String password = userEntity.getPassword();
        boolean isBlocked = !userEntity.getIsActive();

        if(userEntity.verifyIsLockedTemporarily()) {
            isBlocked = true;
            log.warn("User is temporarily blocked");
        }else{
            userService.saveUser(userEntity);
        }

        return User.builder()
            .username(userName)
            .password(password)
            .disabled(isBlocked)
            .roles(role)
            .build();
    }
}
