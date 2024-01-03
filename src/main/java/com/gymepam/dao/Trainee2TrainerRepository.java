package com.gymepam.dao;

import com.gymepam.domain.Trainee2Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Trainee2TrainerRepository extends JpaRepository<Trainee2Trainer, Long> {
}