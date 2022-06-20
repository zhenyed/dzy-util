package io.zhenye.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaCacheTest {

    private static Cache<String, String> cache;

    @BeforeAll
    static void beforeAll() {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(5)
                .maximumSize(10)
                .expireAfterWrite(17, TimeUnit.SECONDS)
                .build();
    }

    @Test
    void testCache() throws ExecutionException {
        String key = "test";

        cache.put(key, "real");
        System.out.println(cache.get(key, () -> "default"));
        cache.invalidate(key);
        System.out.println(cache.get(key, () -> "default"));
    }

}
