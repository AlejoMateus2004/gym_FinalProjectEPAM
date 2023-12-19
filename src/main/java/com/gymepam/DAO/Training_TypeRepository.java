package com.gymepam.DAO;

import com.gymepam.DOMAIN.Training_Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("JPATraining_Type")
public interface Training_TypeRepository extends JpaRepository<Training_Type, Long>, Training_TypeRepo {
}