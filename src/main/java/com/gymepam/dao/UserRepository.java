package com.gymepam.dao;

import com.gymepam.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, UserRepo {
    @Query("SELECT u.userName FROM User u")
    List<String> getAllUsernames();




}