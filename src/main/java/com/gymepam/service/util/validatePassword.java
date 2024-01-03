package com.gymepam.service.util;

import com.gymepam.domain.User;

public interface validatePassword {
    boolean validatePassword(User user, String oldPassword);
}
