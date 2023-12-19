package com.gymepam.DAO;

import com.gymepam.DOMAIN.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository("JPATrainer")
public interface TrainerRepository extends JpaRepository<Trainer, Long>, TrainerRepo {
    @Query("DELETE FROM Trainer t WHERE t.user.userName = :username")
    void deleteByUserUserName(@Param("username") String username);

    @Query("SELECT t FROM Trainer t WHERE t.user.userName = :username")
    Trainer findTrainerByUserUsername(@Param("username") String username);
}