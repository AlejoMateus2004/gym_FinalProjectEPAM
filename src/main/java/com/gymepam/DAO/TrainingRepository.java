package com.gymepam.DAO;

import com.gymepam.DOMAIN.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JPATraining")
public interface TrainingRepository extends JpaRepository<Training, Long>,TrainingRepo {
}