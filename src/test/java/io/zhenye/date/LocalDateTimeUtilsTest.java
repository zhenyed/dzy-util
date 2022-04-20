package io.zhenye.date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeUtilsTest {

    LocalDateTime tempLocalDateTime;

    @BeforeEach
    void setUp() {
        tempLocalDateTime = LocalDateTime.parse("2022-02-03 12:34:56", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void parse() {
        LocalDateTime localDateTime1 = LocalDateTimeUtils.parse("2022-02-03 12:34:56", "yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime2 = LocalDateTime.of(2022, 2, 3, 12, 34, 56);
        assertEquals(localDateTime1, localDateTime2);
    }

    @Test
    void beginOfMonth() {
        LocalDateTime beginOfMonth = LocalDateTime.of(2022, 2, 1, 0, 0, 0);
        assertEquals(beginOfMonth, LocalDateTimeUtils.beginOfMonth(tempLocalDateTime));
    }
}