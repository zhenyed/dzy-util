package io.zhenye.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@Threads(2)
public class JMHCache {

    private static final com.github.benmanes.caffeine.cache.Cache<String, String> caffeineCache =
            Caffeine.newBuilder()
                    .initialCapacity(5)
                    .maximumSize(100)
                    .expireAfterWrite(120, TimeUnit.SECONDS)
                    .build();
    private static final Cache<String, String> guavaCache =
            CacheBuilder.newBuilder()
                    .initialCapacity(5)
                    .maximumSize(100)
                    .expireAfterWrite(120, TimeUnit.SECONDS)
                    .build();
    private static final Map<String, String> hashMapCache = Maps.newHashMap();
    private static final Map<String, String> LHMCache = Maps.newLinkedHashMap();

    private static final String key = "test";

    static {
        caffeineCache.put(key, "real");
        guavaCache.put(key, "real");
        hashMapCache.put(key, "real");
        LHMCache.put(key, "real");
    }

    @Benchmark
    public void caffeineCache() {
        caffeineCache.get(key, (key) -> "empty");
    }

    @Benchmark
    public void guavaCache() throws ExecutionException {
        guavaCache.get(key, () -> "empty");
    }

    @Benchmark
    public void hashMapCache() {
        hashMapCache.get(key);
    }

    @Benchmark
    public void LHMCache() {
        LHMCache.get(key);
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(JMHCache.class.getSimpleName())
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opts).run();
    }

}
