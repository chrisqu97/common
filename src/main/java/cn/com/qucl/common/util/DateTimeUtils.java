package cn.com.qucl.common.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author qucl
 * @date 2020/11/6 10:33
 * 时间工具
 */
public class DateTimeUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static LocalDate getDate() {
        return LocalDate.now();
    }

    public static LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

    public static LocalDate parseDate(String text) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return LocalDate.parse(text, dateTimeFormatter);
    }

    public static LocalDateTime parseDateTime(String text) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return LocalDateTime.parse(text, dateTimeFormatter);
    }

    public static String format(LocalDateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        return dateTimeFormatter.format(dateTime);
    }

    public static String format(LocalDate date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return dateTimeFormatter.format(date);
    }
}
