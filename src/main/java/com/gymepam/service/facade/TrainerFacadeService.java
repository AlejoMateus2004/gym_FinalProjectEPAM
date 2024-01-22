package com.gymepam.service.facade;

import com.gymepam.domain.Login.AuthenticationRequest;
import com.gymepam.domain.Trainer;
import com.gymepam.domain.User;
import com.gymepam.service.TrainerService;
import com.gymepam.service.util.GeneratePassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerFacadeService {

    TrainerService trainerService;

    GeneratePassword generatePassword;

    @Autowired
    public TrainerFacadeService(TrainerService trainerService, GeneratePassword generatePassword) {
        this.trainerService = trainerService;
        this.generatePassword = generatePassword;
    }


    public AuthenticationRequest saveTrainer(Trainer trainer) {
        User user = trainer.getUser();

        String password = generatePassword.generatePassword();
        user.setPassword(password);

        trainer.setUser(user);

        Trainer temp = trainerService.saveTrainer(trainer);
        if (temp == null) {
            return null;
        }
        AuthenticationRequest newAuth = new AuthenticationRequest();

        newAuth.setUsername(temp.getUser().getUserName());
        newAuth.setPassword(password);

        return newAuth;
    }
}
