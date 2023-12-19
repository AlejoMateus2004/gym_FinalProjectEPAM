package com.gymepam.DAO;

import com.gymepam.DOMAIN.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepo {
    Trainer save(Trainer value);
    Optional<Trainer> findById(Long value);
    void delete(Trainer value);
    List<Trainer> findAll();
    Trainer findTrainerByUserUsername(String username);
    void deleteByUserUserName(String username);

}
