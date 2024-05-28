package com.gymepam.service.trainee;

import com.gymepam.dao.TraineeRepo;
import com.gymepam.domain.entities.Trainee;
import com.gymepam.domain.entities.User;
import com.gymepam.service.util.EncryptPassword;
import com.gymepam.service.util.FormatDate;
import com.gymepam.service.util.GenerateUserName;
import com.gymepam.service.util.ValidatePassword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Primary
@Service
public class TraineeService {

    @Autowired
    private TraineeRepo traineeRepository;
    @Autowired
    private ValidatePassword valPassword;
    @Autowired
    private EncryptPassword encryptPass;
    @Autowired
    private GenerateUserName genUserName;
    @Autowired
    private FormatDate formatDate;

    @Transactional
    public Trainee saveTrainee(Trainee trainee) {
        Trainee temp = null;
        try{
            User usr = trainee.getUser();
            String username = genUserName.setUserName(usr);
            usr.setUserName(username);
            usr.setIsActive(true);
            trainee.setUser(usr);
            temp = traineeRepository.findTraineeByUserUsername(username);
            if (temp == null) {
                User user = trainee.getUser();
                String password = user.getPassword();
                user.setPassword(encryptPass.encryptPassword(password));
                trainee.setUser(user);
                log.info("Trainee saved");
                return traineeRepository.save(trainee);
            }

        } catch (Exception e) {
            log.error("Error, trying to save Trainee", e);
        }
        return temp;
    }
    @PreAuthorize("hasRole('ROLE_TRAINEE')")
    @Transactional
    public Trainee updateTrainee(Trainee trainee) {
        Trainee temp = traineeRepository.findTraineeByUserUsername(trainee.getUser().getUserName());
        if (temp == null) {
            log.warn("Trainee can not be updated, is not yet created");
            return null;
        }
        User userUpdates = trainee.getUser();

        User user = temp.getUser();
        user.setFirstName(userUpdates.getFirstName());
        user.setLastName(userUpdates.getLastName());
        user.setIsActive(userUpdates.getIsActive());
        temp.setUser(user);
        temp.setAddress(trainee.getAddress());
        temp.setDateOfBirth(trainee.getDateOfBirth());

        log.info("Trainee updated");
        return traineeRepository.save(temp);
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
            log.info("Trainee not found");
        }else{
            User user = trainee.getUser();
            if(valPassword.validatePassword(user, oldPassword)){
                user.setPassword(encryptPass.encryptPassword(newPassword));
                trainee.setUser(user);
                log.info("Password updated");
                return traineeRepository.save(trainee);
            }
            log.warn("Password is different from old password, password can not be updated");
        }
        return null;
    }


}
