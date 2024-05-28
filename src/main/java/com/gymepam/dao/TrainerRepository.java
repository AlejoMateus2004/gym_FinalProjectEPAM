package com.gymepam.dao;

import com.gymepam.domain.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long>, TrainerRepo {
    void deleteByUserUserName(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.userName = :username")
    Trainer findTrainerByUserUsername(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.isActive = true AND t.traineeList IS EMPTY")
    List<Trainer> findTrainersByUserIsActiveAndTraineeListIsEmpty();

    @Query("SELECT t FROM Trainer t " +
            "WHERE t.user.isActive = true " +
            "AND NOT EXISTS (SELECT 1 FROM t.traineeList trainee WHERE trainee.user.userName = :traineeUsername)")
    Set<Trainer> findActiveTrainersNotAssignedToTrainee(@Param("traineeUsername") String traineeUsername);

}