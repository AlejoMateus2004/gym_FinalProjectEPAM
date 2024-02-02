package com.gymepam.service.util;

import com.gymepam.domain.Roles;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.service.TraineeService;
import com.gymepam.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordMatchingImpl implements UsernamePasswordMatching {

    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordMatching.class);

    @Autowired
    private ValidatePassword validatePass;

    @Autowired
    private TraineeService traineeService;

    @Autowired
    private TrainerService trainerService;

    @Override
    public User getUserByUsernameAndPassword(String username, String password, Roles role) {
        try {
            User user = null;

            switch (role) {
                case TRAINEE:
                    Trainee trainee = traineeService.getTraineeByUserUsername(username);
                    user = (trainee != null) ? trainee.getUser() : null;
                    break;
                case TRAINER:
                    Trainer trainer = trainerService.getTrainerByUserUsername(username);
                    user = (trainer != null) ? trainer.getUser() : null;
                    break;
                default:
                    logger.warn("Unknown role: {}", role);
            }

            if (user == null) {
                logger.warn("User not found: {}", username);
                return null;
            }

            if (validatePass.validatePassword(user, password)) {
                logger.info("Authentication successful for user: {}", username);
                return user;
            } else {
                logger.warn("Invalid credentials for user: {}", username);
                return null;
            }
        } catch (Exception e) {
            logger.error("An error occurred during authentication for user: {}", username, e);
            return null;
        }
    }
}
