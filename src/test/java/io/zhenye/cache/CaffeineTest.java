package io.zhenye.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class CaffeineTest {

    private static Cache<String, String> cache;

    @BeforeAll
    static void beforeAll() {
        cache = Caffeine.newBuilder()
                .initialCapacity(5)
                .maximumSize(10)
                .expireAfterWrite(17, TimeUnit.SECONDS)
                .build();
    }

    @Test
    void testCache() {
        String key = "test";

        cache.put(key, "real");
        System.out.println(cache.get(key, CaffeineTest::getValueFromDB));
        cache.invalidate(key);
        System.out.println(cache.get(key, CaffeineTest::getValueFromDB));
    }

    public static String getValueFromDB(String key) {
        return key + ":default";
    }

}
