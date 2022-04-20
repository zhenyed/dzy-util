package io.zhenye.date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocalDateTimeUtils {

    /**
     * 解析日期时间字符串
     *
     * @param dateTime 日期时间字符串
     * @param pattern  匹配规则
     */
    public static LocalDateTime parse(String dateTime, String pattern) {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当月第一天 00:00:00
     */
    public static LocalDateTime beginOfMonth(LocalDateTime localDateTime) {
        LocalDateTime newLocalDateTime = localDateTime.withDayOfMonth(1);
        return LocalDateTime.of(newLocalDateTime.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取当月最后一天 23:59:59
     */
    public static LocalDateTime lastDayOfMonth(LocalDateTime localDateTime) {
        LocalDateTime newLocalDateTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth());
        return LocalDateTime.of(newLocalDateTime.toLocalDate(), LocalTime.MAX);
    }

}
