package com.gymepam.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class FormatDate {

    public LocalDate getLocalDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (Exception e) {
            log.error("Invalid Date format for " + date);
            return null;
        }
    }
}
