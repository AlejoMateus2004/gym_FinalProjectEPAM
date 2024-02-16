package com.gymepam.service;

import com.gymepam.dao.TrainerRepo;
import com.gymepam.domain.dto.records.TrainerRecord;
import com.gymepam.domain.entities.Trainer;
import com.gymepam.domain.entities.User;
import com.gymepam.service.util.EncryptPassword;
import com.gymepam.service.util.FormatDate;
import com.gymepam.service.util.GenerateUserName;
import com.gymepam.service.util.ValidatePassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class TrainerService {

    @Autowired
    private TrainerRepo trainerRepository;
    @Autowired
    private ValidatePassword valPassword;
    @Autowired
    private EncryptPassword encryptPass;
    @Autowired
    private GenerateUserName genUserName;
    @Autowired
    private FormatDate formatDate;

    @Transactional
    public Trainer saveTrainer(Trainer trainer) {
        Trainer temp = null;
        try{
            User usr = trainer.getUser();
            String username = genUserName.setUserName(usr);
            usr.setUserName(username);
            usr.setIsActive(true);
            trainer.setUser(usr);
            temp = trainerRepository.findTrainerByUserUsername(username);
            if (temp == null) {
                User user = trainer.getUser();
                String password = user.getPassword();
                user.setPassword(encryptPass.encryptPassword(password));
                trainer.setUser(user);
                log.info("Trainer saved");
                return trainerRepository.save(trainer);
            }
        } catch (Exception e) {
            log.error("Error, trying to save Trainer", e);
        }
        return temp;
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional
    public Trainer updateTrainer(Trainer trainer) {
        Trainer temp = trainerRepository.findTrainerByUserUsername(trainer.getUser().getUserName());
        if (temp == null) {
            log.warn("Trainer can not be updated, is not yet created");
            return null;
        }
        User userUpdates = trainer.getUser();

        User user = temp.getUser();
        user.setFirstName(userUpdates.getFirstName());
        user.setLastName(userUpdates.getLastName());
        user.setIsActive(userUpdates.getIsActive());
        temp.setUser(user);
        temp.setTrainingType(trainer.getTrainingType());


        log.info("Trainer updated");
        return trainerRepository.save(temp);
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
            log.info("Trainer not found");
        }else{
            User user = trainer.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(encryptPass.encryptPassword(newPassword));
                trainer.setUser(user);
                log.info("Password updated");
                return trainerRepository.save(trainer);
            }
            log.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional(readOnly = true)
    public List<Trainer> getTrainerByTraineeListEmpty(){
        return trainerRepository.findTrainersByUserIsActiveAndTraineeListIsEmpty();
    }

    @PreAuthorize("hasRole('ROLE_TRAINER') or hasRole('ROLE_TRAINEE')")
    @Transactional(readOnly = true)
    public Set<Trainer> getActiveTrainersNotAssignedToTrainee(String username){
        return trainerRepository.findActiveTrainersNotAssignedToTrainee(username);
    }

    @PreAuthorize("hasRole('ROLE_TRAINER')")
    @Transactional(readOnly = true)
    public Trainer getTrainerByUserUsernameWithTrainingParams(TrainerRecord.TrainerRequestWithTrainingParams trainerRequest) {
        return trainerRepository.findTrainerByUserUsernameWithTrainingParams(
                trainerRequest.trainer_username(),
                trainerRequest.trainingRequest().periodFrom(),
                trainerRequest.trainingRequest().periodTo(),
                trainerRequest.trainingRequest().user_name()
        );
    }



}
