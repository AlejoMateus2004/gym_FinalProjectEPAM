package com.gymepam.service.security;

import com.gymepam.domain.Roles;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainer;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserSecurityService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserSecurityService.class);

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private ApplicationContext context;


    @Autowired
    public UserSecurityService(TraineeService traineeService, TrainerService trainerService, ApplicationContext context) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.context = context;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String role = "";
        com.gymepam.domain.User userEntity = null;


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
            logger.error("Error loading user by username: {}", username);
//            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        String userName = userEntity.getUserName();
        String password = userEntity.getPassword();
        boolean isActive = !userEntity.getIsActive();

        return User.builder()
            .username(userName)
            .password(password)
            .disabled(isActive)
            .roles(role)
            .build();

    }
}
