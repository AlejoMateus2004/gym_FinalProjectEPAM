package com.gymepam.service.util;

import com.gymepam.domain.Roles;
import com.gymepam.domain.User;

public interface UsernamePasswordMatching {
    User getUserByUsernameAndPassword(String username, String password, Roles role);
}
