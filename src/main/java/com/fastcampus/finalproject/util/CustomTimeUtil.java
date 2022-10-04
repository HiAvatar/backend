package com.fastcampus.finalproject.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomTimeUtil {

    private CustomTimeUtil() {}

    public static String convertDateTime(ZonedDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
