package com.gymepam.SERVICE;

import com.gymepam.DAO.UserRepo;
import com.gymepam.DOMAIN.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static UserRepo UserRepository;

    @Autowired
    public UserService(@Qualifier("InMemoryUser") UserRepo UserRepository) {
        this.UserRepository = UserRepository;
    }

    public void setUserRepository(UserRepo UserRepository) {
        this.UserRepository = UserRepository;
    }

    public User saveUser(User User) {
        return UserRepository.save(User);
    }

    public User getUser(Long UserId) {
        return UserRepository.findById(UserId).orElse(null);
    }

    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    public List<String> getAllUserNames() {
        return UserRepository.getAllUsernames();
    }

}
