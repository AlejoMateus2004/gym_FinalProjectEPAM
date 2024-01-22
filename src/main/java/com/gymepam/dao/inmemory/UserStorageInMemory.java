package com.gymepam.dao.inmemory;

import com.gymepam.dao.UserRepo;
import com.gymepam.domain.User;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

public class UserStorageInMemory implements UserRepo {

    private static Map<Long, User> UserMap = new HashMap<>();

    @Override
    public User save(User value) {
        if (value == null) {
            return null;
        }
        UserMap.put(value.getId(), value);
        return UserMap.get(value.getId());
    }

    @Override
    public Optional<User> findById(Long value) {
        return Optional.ofNullable(UserMap.get(value));
    }

    @Override
    public Optional<User> findByUserName(String username) {
            List<User> userList = new ArrayList<>(UserMap.values());
        return userList.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();
    }

    @Override
    public void delete(User value) {
        UserMap.remove(value.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(UserMap.values());
    }

    @Override
    public List<String> getAllUsernames() {
        List<User> usersList = new ArrayList<>(UserMap.values());
        return  usersList.stream()
                .map(User::getUserName)
                .collect(Collectors.toList());
    }


}
