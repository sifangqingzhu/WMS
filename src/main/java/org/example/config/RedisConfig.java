package org.example.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis配置类 - 使用Redisson
 */
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.database:0}")
    private int database;

    @Value("${spring.data.redis.timeout:5000ms}")
    private String timeout;

    /**
     * 配置Redisson客户端
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();

        String address = "redis://" + host + ":" + port;

        config.useSingleServer()
                .setAddress(address)
                .setDatabase(database)
                .setTimeout(parseTimeout(timeout))
                .setConnectionPoolSize(64)
                .setConnectionMinimumIdleSize(10);

        // 如果密码不为空，则设置密码
        if (password != null && !password.isEmpty()) {
            config.useSingleServer().setPassword(password);
        }

        return Redisson.create(config);
    }

    /**
     * 解析超时时间字符串（支持ms, s等单位）
     */
    private int parseTimeout(String timeoutStr) {
        if (timeoutStr == null || timeoutStr.isEmpty()) {
            return 5000;
        }

        timeoutStr = timeoutStr.toLowerCase().trim();

        if (timeoutStr.endsWith("ms")) {
            return Integer.parseInt(timeoutStr.substring(0, timeoutStr.length() - 2));
        } else if (timeoutStr.endsWith("s")) {
            return Integer.parseInt(timeoutStr.substring(0, timeoutStr.length() - 1)) * 1000;
        } else {
            return Integer.parseInt(timeoutStr);
        }
    }
}
