package org.example.service;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Redis服务类 - 封装Token黑名单管理所需的Redis操作
 */
@Service
public class RedisService {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 设置字符串值（带过期时间）
     * 用于Token黑名单存储
     */
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofMillis(timeUnit.toMillis(timeout)));
    }

    /**
     * 判断key是否存在
     * 用于检查Token是否在黑名单中
     */
    public boolean hasKey(String key) {
        return redissonClient.getBucket(key).isExists();
    }
}
