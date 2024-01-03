package com.gymepam.service;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.Trainee;
import com.gymepam.domain.User;
import com.gymepam.service.util.validatePassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeRepo traineeRepository;

    @Autowired
    private validatePassword valPassword;

    public void setTraineeRepository(TraineeRepo traineeRepository) {
        this.traineeRepository = traineeRepository;
    }
    @Transactional
    public Trainee saveTrainee(Trainee trainee) {
        Trainee temp = traineeRepository.findTraineeByUserUsername(trainee.getUser().getUserName());
        if (temp == null) {
            logger.info("Trainee saved");
            return traineeRepository.save(trainee);
        }else{
            User user = temp.getUser();
            String oldPassword = trainee.getUser().getPassword();
            if(valPassword.validatePassword(user, oldPassword)){
                logger.info("Trainee updated");
                return traineeRepository.save(trainee);
            }
            logger.warn("Password is different from old password, trainee can not be updated");
            return null;
        }
    }
    @Transactional(readOnly = true)
    public Trainee getTrainee(Long traineeId) {
        return traineeRepository.findById(traineeId).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<Trainee> getAllTrainees() {
        return traineeRepository.findAll();
    }
    @Transactional
    public void deleteTrainee(Trainee trainee) {
        traineeRepository.delete(trainee);
    }
    @Transactional(readOnly = true)
    public Trainee getTraineeByUserUsername(String username){
        return traineeRepository.findTraineeByUserUsername(username);
    }
    @Transactional
    public void deleteByUserUserName(String username){
        traineeRepository.deleteByUserUserName(username);
    }
    @Transactional
    public Trainee updatePassword(String username, String oldPassword, String newPassword){
        Trainee trainee = traineeRepository.findTraineeByUserUsername(username);
        if (trainee == null) {
            logger.info("Trainee not found");
        }else{
            User user = trainee.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(newPassword);
                trainee.setUser(user);
                logger.info("Password updated");
                return traineeRepository.save(trainee);
            }
            logger.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }
}
