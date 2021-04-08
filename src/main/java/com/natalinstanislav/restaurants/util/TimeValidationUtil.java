package com.natalinstanislav.restaurants.util;

import com.natalinstanislav.restaurants.util.exception.TimeValidationException;

import java.time.LocalTime;

public class TimeValidationUtil {

    private TimeValidationUtil() {
    }

    public static void checkVoteTime() {
        if (LocalTime.now().isAfter(LocalTime.of(11, 0))) {
            throw new TimeValidationException("Can't revote after 11:00");
        }
    }
}
