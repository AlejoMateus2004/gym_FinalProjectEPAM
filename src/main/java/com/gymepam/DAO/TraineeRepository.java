package com.gymepam.DAO;

import com.gymepam.DOMAIN.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("JPATrainee")
public interface TraineeRepository extends JpaRepository<Trainee, Long>, TraineeRepo{
    @Query("DELETE FROM Trainee t WHERE t.user.userName = :username")
    void deleteByUserUserName(@Param("username") String username);

    @Query("SELECT t FROM Trainee t WHERE t.user.userName = :username")
    Trainee findTraineeByUserUsername(@Param("username") String username);
}