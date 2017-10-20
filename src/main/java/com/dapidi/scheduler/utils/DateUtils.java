package com.dapidi.scheduler.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(String target, String dateFormat) throws ParseException {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return df.parse(target);
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date now() {
        return asDate(LocalDateTime.now());
    }

    public static Date nowPlusDays(long days) {
        return asDate(LocalDateTime.now().plusDays(days));
    }
}
