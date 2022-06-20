package io.zhenye.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.BitSet;

public class BitMap {

    private static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://zhenye.host:9004")
                .setPassword("AQ72mmWugML3mZiqQ2yz")
                .setDatabase(1);
        redissonClient = Redisson.create(config);
    }

    public static void main(String[] args) {
        RBitSet bitSet = redissonClient.getBitSet("test");
        bitSet.delete();
        long key = 0;
        System.out.println("get key: " + bitSet.get(key));
        bitSet.set(key);
        System.out.println("get key: " + bitSet.get(key));
    }

}
