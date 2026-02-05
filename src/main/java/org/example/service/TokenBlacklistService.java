package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Token黑名单服务类
 * 用于管理已登出的Token，防止被重复使用
 */
@Service
public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);

    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    @Autowired
    private RedisService redisService;

    /**
     * 将Token加入黑名单
     *
     * @param token Token字符串
     * @param expirationTime Token过期时间
     */
    public void addToBlacklist(String token, Date expirationTime) {
        if (token == null || token.trim().isEmpty()) {
            log.warn("尝试将空Token加入黑名单");
            return;
        }

        try {
            String key = TOKEN_BLACKLIST_PREFIX + token;

            // 计算Token剩余有效时间（毫秒）
            long currentTime = System.currentTimeMillis();
            long expirationMillis = expirationTime.getTime();
            long ttl = expirationMillis - currentTime;

            // 如果Token已经过期，不需要加入黑名单
            if (ttl <= 0) {
                log.info("Token已过期，无需加入黑名单");
                return;
            }

            // 将Token存入Redis，过期时间与Token原本过期时间对齐
            redisService.set(key, "logout", ttl, TimeUnit.MILLISECONDS);

            log.info("Token已加入黑名单，剩余有效期: {} 秒", ttl / 1000);

        } catch (Exception e) {
            log.error("将Token加入黑名单失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 检查Token是否在黑名单中
     *
     * @param token Token字符串
     * @return true表示在黑名单中（已登出），false表示不在黑名单中
     */
    public boolean isBlacklisted(String token) {
        if (token == null || token.trim().isEmpty()) {
            return false;
        }

        try {
            String key = TOKEN_BLACKLIST_PREFIX + token;
            boolean exists = redisService.hasKey(key);

            if (exists) {
                log.debug("Token在黑名单中，已被登出");
            }

            return exists;

        } catch (Exception e) {
            log.error("检查Token黑名单状态失败: {}", e.getMessage(), e);
            // 为了安全起见，如果检查失败，返回true（拒绝访问）
            return true;
        }
    }
}
