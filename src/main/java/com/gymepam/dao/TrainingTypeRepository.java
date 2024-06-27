package com.gymepam.dao;

import com.gymepam.domain.entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long>, TrainingTypeRepo {
}