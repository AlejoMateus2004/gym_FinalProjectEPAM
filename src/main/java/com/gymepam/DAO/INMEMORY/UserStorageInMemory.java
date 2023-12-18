package com.gymepam.DAO.INMEMORY;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("InMemoryUser")
public class UserStorageInMemory implements Repo<User> {

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
    public void delete(User value) {
        UserMap.remove(value.getId());
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(UserMap.values());
    }
}
