package com.gymepam.dao;

import com.gymepam.domain.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Long>, TrainingTypeRepo {
}