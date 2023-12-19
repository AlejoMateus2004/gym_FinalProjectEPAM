package com.gymepam.DAO;

import com.gymepam.DOMAIN.Trainee;
import com.gymepam.DOMAIN.User;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TraineeRepo {
    Trainee save(Trainee value);
    Optional<Trainee> findById(Long value);
    void delete(Trainee value);
    List<Trainee> findAll();
    Trainee findTraineeByUserUsername(String username);
    void deleteByUserUserName(String username);

}
