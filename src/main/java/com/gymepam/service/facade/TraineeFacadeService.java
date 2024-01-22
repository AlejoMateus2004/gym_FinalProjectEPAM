package com.gymepam.service.facade;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.User;
import com.gymepam.service.TraineeService;
import com.gymepam.service.util.GeneratePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TraineeFacadeService {

    TraineeService traineeService;

    GeneratePassword generatePassword;

    @Autowired
    public TraineeFacadeService(TraineeService traineeService, GeneratePassword generatePassword) {
        this.traineeService = traineeService;
        this.generatePassword = generatePassword;
    }


    public AuthenticationRequest saveTrainee(Trainee trainee) {
        User user = trainee.getUser();

        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainee.setUser(user);

        Trainee temp = traineeService.saveTrainee(trainee);
        if (temp == null) {
            return null;
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return newAuth;
    }
}
