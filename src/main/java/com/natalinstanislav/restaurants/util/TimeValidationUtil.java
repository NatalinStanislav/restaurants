package com.natalinstanislav.restaurants.util;

import com.natalinstanislav.restaurants.util.exception.TimeValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeValidationUtil {

    private TimeValidationUtil() {
    }

    public static LocalDate checkVoteTime(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        if (time.isAfter(LocalTime.of(11, 0))) {
            throw new TimeValidationException("Can't vote after 11:00");
        }
        return dateTime.toLocalDate();
    }

    public static void checkVoteDate(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        if (!date.isEqual(LocalDate.now())) {
            throw new TimeValidationException("Can't vote another date");
        }
    }
}
