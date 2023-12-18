package com.gymepam.DAO.INMEMORY;

import com.gymepam.DAO.Repo;
import com.gymepam.DOMAIN.Training_Type;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("InMemoryTraining_Type")
public class Training_TypeStorageInMemory implements Repo<Training_Type> {

    private static Map<Long, Training_Type> Training_TypeMap = new HashMap<>();

    @Override
    public Training_Type save(Training_Type value) {
        if (value == null) {
            return null;
        }
        Training_TypeMap.put(value.getId(), value);
        return Training_TypeMap.get(value.getId());
    }

    @Override
    public Optional<Training_Type> findById(Long value) {
        return Optional.ofNullable(Training_TypeMap.get(value));
    }

    @Override
    public void delete(Training_Type value) {
        Training_TypeMap.remove(value.getId());
    }

    @Override
    public List<Training_Type> findAll() {
        return new ArrayList<>(Training_TypeMap.values());
    }
}
