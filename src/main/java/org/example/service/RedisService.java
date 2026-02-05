package org.example.service;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务类 - 封装常用的Redis操作
 */
@Service
@SuppressWarnings("unused")
public class RedisService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 设置字符串值
     */
    public void set(String key, Object value) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置字符串值（带过期时间）
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // 使用新的API：Duration替代已废弃的set(value, timeout, timeUnit)
        bucket.set(value, Duration.ofMillis(timeUnit.toMillis(timeout)));
    }

    /**
     * 获取字符串值
     */
    public Object get(String key) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除key
     */
    public boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 判断key是否存在
     */
    public boolean hasKey(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 设置key过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit timeUnit) {
        // 使用新的API：Duration替代已废弃的expire(timeout, timeUnit)
        return redissonClient.getBucket(key).expire(Duration.ofMillis(timeUnit.toMillis(timeout)));
    }

    /**
     * 获取Map操作对象
     */
    public <K, V> RMap<K, V> getMap(String key) {
        return redissonClient.getMap(key);
    }

    /**
     * Hash设置值
     */
    public <K, V> void hset(String key, K hashKey, V value) {
        RMap<K, V> map = redissonClient.getMap(key);
        map.put(hashKey, value);
    }

    /**
     * Hash获取值
     */
    public <K, V> V hget(String key, K hashKey) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.get(hashKey);
    }

    /**
     * Hash删除值
     */
    public <K, V> V hdel(String key, K hashKey) {
        RMap<K, V> map = redissonClient.getMap(key);
        return map.remove(hashKey);
    }

    /**
     * 获取分布式锁
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        try {
            return redissonClient.getLock(lockKey).tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    /**
     * 释放分布式锁
     */
    public void unlock(String lockKey) {
        redissonClient.getLock(lockKey).unlock();
    }
}
