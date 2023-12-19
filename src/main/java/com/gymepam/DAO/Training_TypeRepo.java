package com.gymepam.DAO;

import com.gymepam.DOMAIN.Training_Type;

import java.util.List;
import java.util.Optional;

public interface Training_TypeRepo {
    Training_Type save(Training_Type value);
    Optional<Training_Type> findById(Long value);
    void delete(Training_Type value);
    List<Training_Type> findAll();


}
