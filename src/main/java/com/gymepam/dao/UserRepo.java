package com.gymepam.dao;

import com.gymepam.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    User save(User value);
    Optional<User> findById(Long value);
    Optional<User> findByUserName(String username);
    void delete(User value);
    List<User> findAll();
    List<String> getAllUsernames();
}
