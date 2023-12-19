package com.gymepam.DAO;

import com.gymepam.DOMAIN.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    User save(User value);
    Optional<User> findById(Long value);
    void delete(User value);
    List<User> findAll();
    List<String> getAllUsernames();
}
