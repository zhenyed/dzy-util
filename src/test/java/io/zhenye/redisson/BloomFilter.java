package io.zhenye.redisson;

import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class BloomFilter {

    private static final RedissonClient redissonClient;

    static {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://192.168.75.62:6379")
                .setDatabase(1);
        redissonClient = Redisson.create(config);
    }

    public RBloomFilter<String> init(String key){
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(key);
        bloomFilter.tryInit(1000L, 0.01);
        return bloomFilter;
    }

    public void add(String key, String value) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(key);
        bloomFilter.add(value);
    }

    public boolean contains(String key, String value) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(key);
        return bloomFilter.contains(value);
    }

    public static void main(String[] args) {
        String key = "aaa";
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.init(key);
//        for (int i = 0; i < 10_00; i++) {
//            if (i != 123) {
//                bloomFilter.add(key, String.valueOf(i));
//            }
//        }
//        System.out.println("Bloom filter is contain 100: " + bloomFilter.contains(key, "100"));
//        System.out.println("Bloom filter is contain 123: " + bloomFilter.contains(key, "123"));
    }

}
