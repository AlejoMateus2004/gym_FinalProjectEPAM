package com.gymepam.service.user;

import com.gymepam.dao.UserRepo;
import com.gymepam.domain.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepository;


    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUserName(username).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<String> getAllUserNames() {
        return userRepository.getAllUsernames();
    }

}
