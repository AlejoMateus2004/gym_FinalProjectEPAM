package com.gymepam.service;

import com.gymepam.dao.UserRepo;
import com.gymepam.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;

    public void setuserRepository(UserRepo userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public User saveUser(User User) {
        return userRepository.save(User);
    }
    @Transactional(readOnly = true)
    public User getUser(Long UserId) {
        return userRepository.findById(UserId).orElse(null);
    }
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElse(null);
    }
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional(readOnly = true)
    public List<String> getAllUserNames() {
        return userRepository.getAllUsernames();
    }

}
