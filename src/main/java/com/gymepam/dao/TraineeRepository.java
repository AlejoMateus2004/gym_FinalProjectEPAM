package com.gymepam.dao;

import com.gymepam.domain.entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Long>, TraineeRepo{
    void deleteByUserUserName(@Param("username") String username);

    @Query("SELECT t FROM Trainee t WHERE t.user.userName = :username")
    Trainee findTraineeByUserUsername(@Param("username") String username);

    @Query("SELECT DISTINCT t FROM Trainee t " +
            "LEFT JOIN FETCH t.trainingList tr " +
            "WHERE t.user.userName = :userName " +
            "AND (:periodFrom IS NULL OR tr.trainingDate >= :periodFrom) " +
            "AND (:periodTo IS NULL OR tr.trainingDate <= :periodTo) " +
            "AND (COALESCE(:trainerName, '') = '' OR LOWER(tr.trainer.user.firstName) LIKE LOWER(:trainerName)) " +
            "AND (COALESCE(:trainingType, '') = '' OR LOWER(tr.trainingType.trainingTypeName) LIKE LOWER(:trainingType))")
    Trainee findTraineeByUserUsernameWithTrainingParams(
            @Param("userName") String userName,
            @Param("periodFrom") LocalDate periodFrom,
            @Param("periodTo") LocalDate periodTo,
            @Param("trainerName") String trainerName,
            @Param("trainingType") String trainingType
    );
}