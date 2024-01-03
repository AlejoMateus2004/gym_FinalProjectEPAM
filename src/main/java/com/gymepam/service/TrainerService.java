package com.gymepam.service;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.Trainer;
import com.gymepam.domain.User;
import com.gymepam.service.util.validatePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerRepo trainerRepository;
    @Autowired
    private validatePassword valPassword;

    public void setTrainerRepository(TrainerRepo trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
    @Transactional
    public Trainer saveTrainer(Trainer trainer) {
        Trainer temp = trainerRepository.findTrainerByUserUsername(trainer.getUser().getUserName());
        if (temp == null) {
            logger.info("Trainer saved");
            return trainerRepository.save(trainer);
        }else{
            User user = temp.getUser();
            String oldPassword = trainer.getUser().getPassword();
            if(valPassword.validatePassword(user, oldPassword)){
                logger.info("Trainer updated");
                return trainerRepository.save(trainer);
            }
            logger.warn("Password is different from old password, trainer can not be updated");
            return null;
        }
    }
    @Transactional(readOnly = true)
    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Trainer getTrainerByUserUsername(String username){
        return trainerRepository.findTrainerByUserUsername(username);
    }
    @Transactional
    public Trainer updatePassword(String username, String oldPassword, String newPassword){
        Trainer trainer = trainerRepository.findTrainerByUserUsername(username);
        if (trainer == null) {
            logger.info("Trainer not found");
        }else{
            User user = trainer.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(newPassword);
                trainer.setUser(user);
                logger.info("Password updated");
                return trainerRepository.save(trainer);
            }
            logger.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }


}
