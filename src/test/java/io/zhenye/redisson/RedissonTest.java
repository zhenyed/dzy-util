package io.zhenye.redisson;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class RedissonTest {

    private static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.setCodec(new StringCodec("GBK"));
        config.useSingleServer()
                .setAddress("redis://localhost:6379")
                .setDatabase(0);
        redissonClient = Redisson.create(config);
    }

    @Test
    void count() {
        String queueName = "delayQueue";
        RBlockingQueue<String> queue = redissonClient.getBlockingQueue(queueName);
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(queue);

        long start = System.currentTimeMillis();
        delayedQueue.offer("first", 4, TimeUnit.SECONDS);
        delayedQueue.offer("second", 7, TimeUnit.SECONDS);
        delayedQueue.offer("third", 8, TimeUnit.SECONDS);

        while (true) {
            List<String> poll = queue.poll(10);
            if (CollectionUtils.isNotEmpty(poll)) {
                System.out.println(poll);
            }
            long cur = System.currentTimeMillis() - start;
            System.out.println(cur + "ms");

            if (cur >= 1000 * 10) {
                return;
            }
        }
    }

}