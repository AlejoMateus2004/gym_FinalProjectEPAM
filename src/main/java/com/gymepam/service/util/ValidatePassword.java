package com.gymepam.service.util;

import com.gymepam.domain.entities.User;

public interface ValidatePassword {
    boolean validatePassword(User user, String oldPassword);
}
