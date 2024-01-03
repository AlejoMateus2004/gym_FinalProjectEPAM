package com.gymepam.dao;

import com.gymepam.domain.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long>,TrainingRepo {

    @Query("SELECT tr FROM Training tr WHERE tr.trainee.user.userName = :username")
    List<Training> findTrainingByTrainee(@Param("username") String username);

    @Query("SELECT tr FROM Training tr WHERE tr.trainer.user.userName = :username")
    List<Training> findTrainingByTrainer(@Param("username") String username);
}