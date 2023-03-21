package com.example.services.cache;

import com.example.model.Activity;
import java.util.List;
import java.util.concurrent.Future;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class CacheHandlerImpl implements CacheHandler<String, List<Activity>> {

    private final RedissonClient redissonClient;

    public CacheHandlerImpl(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public List<Activity> get(String key) {
        RBucket<List<Activity>> result = redissonClient.getBucket(key);
        return result.get();
    }

    @Override
    public Future<List<Activity>> getAsync(String key) {
        RBucket<List<Activity>> result = redissonClient.getBucket(key);
        return result.getAsync();
    }

    @Override
    public void put(String key, List<Activity> value) {
        RBucket<List<Activity>> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    @Override
    public void putAsync(String key, List<Activity> value) {
        RBucket<List<Activity>> bucket = redissonClient.getBucket(key);
        bucket.setAsync(value);
    }
}
