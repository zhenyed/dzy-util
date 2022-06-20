package io.zhenye.redisson;

import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RHyperLogLog;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class HyperLogLogTest {

    private static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.setCodec(new StringCodec("GBK"));
        config.useSingleServer()
                .setAddress("redis://zhenye.host:9004")
                .setPassword("AQ72mmWugML3mZiqQ2yz")
                .setDatabase(1);
        redissonClient = Redisson.create(config);
    }

    @Test
    void count() {
        RHyperLogLog<String> hyperLogLog = redissonClient.getHyperLogLog("hyperLogLog");
        hyperLogLog.delete();

        int count = 100_000;
        List<String> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add("" + (RandomUtils.nextInt(0, 1000)));
        }

        hyperLogLog.addAll(list);

        System.out.println("Actually count: " + new HashSet<>(list).size());
        System.out.println("hyperLogLog count: " + hyperLogLog.count());
    }

    @Test
    void countWith() {
        int count = 100_000;

        RHyperLogLog<String> hyperLogLog1 = redissonClient.getHyperLogLog("hyperLogLog1");
        hyperLogLog1.delete();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list1.add("" + (RandomUtils.nextInt(0, 500)));
        }
        hyperLogLog1.addAll(list1);

        RHyperLogLog<String> hyperLogLog2 = redissonClient.getHyperLogLog("hyperLogLog2");
        hyperLogLog2.delete();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list2.add("" + (RandomUtils.nextInt(0, 1000)));
        }
        hyperLogLog2.addAll(list2);

        System.out.println("hyperLogLog countWith: " + hyperLogLog1.countWith("hyperLogLog2"));
    }

    @Test
    void merge() {
        int count = 100_000;

        RHyperLogLog<String> hyperLogLog1 = redissonClient.getHyperLogLog("hyperLogLog1");
        hyperLogLog1.delete();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list1.add("" + (RandomUtils.nextInt(0, 500)));
        }
        hyperLogLog1.addAll(list1);
        System.out.println("hyperLogLog1 count: " + hyperLogLog1.count());

        RHyperLogLog<String> hyperLogLog2 = redissonClient.getHyperLogLog("hyperLogLog2");
        hyperLogLog2.delete();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list2.add("" + (RandomUtils.nextInt(0, 1000)));
        }
        hyperLogLog2.addAll(list2);
        System.out.println("hyperLogLog2 count: " + hyperLogLog2.count());

        hyperLogLog1.mergeWith("hyperLogLog2");
        System.out.println("hyperLogLog merge1: " + hyperLogLog1.count());
        System.out.println("hyperLogLog merge2: " + hyperLogLog2.count());
    }

    @Test
    void test() {
        RList<String> list = redissonClient.getList("list");
        list.delete();
        list.add("哈哈哈");
    }

}
