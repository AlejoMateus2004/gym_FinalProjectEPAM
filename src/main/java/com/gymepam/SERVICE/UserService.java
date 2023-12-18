package com.gymepam.SERVICE;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static Repo<User> UserRepository;

    @Autowired
    public UserService(@Qualifier("InMemoryUser") Repo<User> UserRepository) {
        this.UserRepository = UserRepository;
    }

    public void setUserRepository(Repo<User> UserRepository) {
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
        return UserRepository.findAll()
                .stream()
                .map(User::getUserName)
                .collect(Collectors.toList());
    }

}
