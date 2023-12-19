package com.gymepam.SERVICE.UTIL;

import com.gymepam.SERVICE.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("manualUserName")
public class generateUserNameImpl implements generateUserName {

    private static final Logger logger = LoggerFactory.getLogger(generateUserNameImpl.class);
    @Autowired
    private UserService userService;
    @Cacheable("usernameCounts")
    public Map<String, Integer> getUsernameCounts() {
        try {
            List<String> allUserNames = userService.getAllUserNames();

            if (allUserNames == null || allUserNames.isEmpty()) {
                return Collections.emptyMap();
            }

            return allUserNames.stream()
                    .collect(Collectors.groupingBy(
                            key -> key,
                            Collectors.summingInt(e -> 1)
                    ));
        } catch (Exception e) {
            logger.error("Error retrieving username counts", e);
            throw new RuntimeException("Error retrieving username counts", e);
        }
    }

    @Override
    public String generateUserName(String firstName, String lastName) {

        try{
            Map<String, Integer> usernameCounts = getUsernameCounts();

            String baseUsername = (firstName + "." + lastName).toLowerCase();
            String username = baseUsername;

            if (usernameCounts != null) {
                int count = 1;
                while (usernameCounts.containsKey(username)) {
                    count++;
                    username = baseUsername + count;
                }
            }
            logger.info("Generated username");
            return username;
        } catch(Exception e){
            logger.error("Error generating username for {} {}", firstName, lastName, e);
            throw new RuntimeException("Error generating username", e);
        }
    }
    @Override
    public boolean isValidUsername(String username, String firstName, String lastName) {
        try{
            Map<String, Integer> usernameCounts = getUsernameCounts();

            if (username == null || username.isEmpty()) {
                return false;
            }

            String baseUserName = (firstName + "." + lastName).toLowerCase();

            if (!username.contains(baseUserName) || !username.matches("^[a-z0-9.]+$")) {
                return false;
            }

            if (usernameCounts!= null && usernameCounts.containsKey(username)) {
                return false;
            }

            return true;

        }catch(Exception e){
            logger.error("Error validating username {}", username, e);
            throw new RuntimeException("Error validating username", e);
        }
    }

}
