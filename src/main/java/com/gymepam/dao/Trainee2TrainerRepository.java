package com.gymepam.dao;

import com.gymepam.domain.Trainee;
import com.gymepam.domain.Trainee2Trainer;
import com.gymepam.domain.Trainer;
import com.gymepam.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Trainee2TrainerRepository extends JpaRepository<Trainee2Trainer, Long>, Trainee2TrainerRepo {

}