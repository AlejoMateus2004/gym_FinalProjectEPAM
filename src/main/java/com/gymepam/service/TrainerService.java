package com.gymepam.service;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.Trainer;
import com.gymepam.domain.User;
import com.gymepam.service.util.encryptPassword;
import com.gymepam.service.util.generateUserName;
import com.gymepam.service.util.ValidatePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    private TrainerRepo trainerRepository;
    @Autowired
    private ValidatePassword valPassword;
    @Autowired
    private encryptPassword encryptPass;
    @Autowired
    private generateUserName genUserName;


    public void setTrainerRepository(TrainerRepo trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
    @Transactional
    public Trainer saveTrainer(Trainer trainer) {
        try{
            User usr = trainer.getUser();
            String username = genUserName.setUserName(usr);
            usr.setUserName(username);
            trainer.setUser(usr);
            Trainer temp = trainerRepository.findTrainerByUserUsername(username);
            if (temp == null) {
                User user = trainer.getUser();
                String password = user.getPassword();
                user.setPassword(encryptPass.encryptPassword(password));
                trainer.setUser(user);
                logger.info("Trainer saved");
                return trainerRepository.save(trainer);
            }
        } catch (Exception e) {
            logger.error("Error, trying to save Trainer", e);
        }
        return null;
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        Trainer temp = trainerRepository.findTrainerByUserUsername(trainer.getUser().getUserName());
        if (temp == null) {
            logger.warn("Trainer can not be updated, is not yet created");
            return null;
        }
        User user = temp.getUser();
        String oldPassword = trainer.getUser().getPassword();
        if((oldPassword.equals(user.getPassword()))){
            logger.info("Trainee updated");
            return trainerRepository.save(trainer);
        }
        logger.warn("Password is different from old password, trainer can not be updated");
        return null;
    }
    @Transactional(readOnly = true)
    public Trainer getTrainer(Long trainerId) {
        return trainerRepository.findById(trainerId).orElse(null);
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional(readOnly = true)
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Trainer getTrainerByUserUsername(String username){
        return trainerRepository.findTrainerByUserUsername(username);
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional
    public Trainer updatePassword(String username, String oldPassword, String newPassword){
        Trainer trainer = trainerRepository.findTrainerByUserUsername(username);
        if (trainer == null) {
            logger.info("Trainer not found");
        }else{
            User user = trainer.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(encryptPass.encryptPassword(newPassword));
                trainer.setUser(user);
                logger.info("Password updated");
                return trainerRepository.save(trainer);
            }
            logger.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional(readOnly = true)
    public List<Trainer> getTrainerByTraineeListEmpty(){
        return trainerRepository.findTrainersByUserIsActiveAndTraineeListIsEmpty();
    }


}
