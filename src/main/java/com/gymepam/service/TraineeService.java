package com.gymepam.service;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.User;
import com.gymepam.service.util.EncryptPassword;
import com.gymepam.service.util.GenerateUserName;
import com.gymepam.service.util.ValidatePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeRepo traineeRepository;
    @Autowired
    private ValidatePassword valPassword;
    @Autowired
    private EncryptPassword encryptPass;
    @Autowired
    private GenerateUserName genUserName;

    public void setTraineeRepository(TraineeRepo traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
    @Transactional
    public Trainee saveTrainee(Trainee trainee) {
        try{
            User usr = trainee.getUser();
            String username = genUserName.setUserName(usr);
            usr.setUserName(username);
            usr.setIsActive(true);
            trainee.setUser(usr);
            Trainee temp = traineeRepository.findTraineeByUserUsername(username);
            if (temp == null) {
                User user = trainee.getUser();
                String password = user.getPassword();
                user.setPassword(encryptPass.encryptPassword(password));
                trainee.setUser(user);
                logger.info("Trainee saved");
                return traineeRepository.save(trainee);
            }

        } catch (Exception e) {
            logger.error("Error, trying to save Trainee", e);

        }
        return null;
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        Trainee temp = traineeRepository.findTraineeByUserUsername(trainee.getUser().getUserName());
        if (temp == null) {
            logger.warn("Trainee can not be updated, is not yet created");
            return null;
        }
        User user = temp.getUser();
        String oldPassword = trainee.getUser().getPassword();
        if((oldPassword.equals(user.getPassword()))){
            logger.info("Trainee updated");
            return traineeRepository.save(trainee);
        }
        logger.warn("Password is different from old password, trainee can not be updated");
        return null;
    }

    @Transactional(readOnly = true)
    public Trainee getTrainee(Long traineeId) {
        return traineeRepository.findById(traineeId).orElse(null);
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional
    public void deleteTrainee(Trainee trainee) {
        traineeRepository.delete(trainee);
    }

    @Transactional(readOnly = true)
    public Trainee getTraineeByUserUsername(String username){
        return traineeRepository.findTraineeByUserUsername(username);
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional
    public void deleteByUserUserName(String username){
        traineeRepository.deleteByUserUserName(username);
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional
    public Trainee updatePassword(String username, String oldPassword, String newPassword){
        Trainee trainee = traineeRepository.findTraineeByUserUsername(username);
        if (trainee == null) {
            logger.info("Trainee not found");
        }else{
            User user = trainee.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(encryptPass.encryptPassword(newPassword));
                trainee.setUser(user);
                logger.info("Password updated");
                return traineeRepository.save(trainee);
            }
            logger.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }
}
