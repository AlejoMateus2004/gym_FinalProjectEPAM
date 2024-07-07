package com.gymepam.dao;

import com.gymepam.domain.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long>, TrainingTypeRepo {
}