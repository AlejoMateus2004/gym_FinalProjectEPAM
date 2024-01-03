package com.gymepam.SERVICE.UTIL;

public interface generateUserName {
    String generateUserName(String firstName, String lastName);
    boolean isValidUsername(String username, String FirstName, String LastName);
}
