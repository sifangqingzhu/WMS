# Redis配置说明

## 配置完成

已成功为项目配置Redisson客户端，用于操作Redis。

## 配置文件

### 1. .env 文件
Redis相关配置已添加到 `.env` 文件中：

```properties
# Redis Configuration
REDIS_HOST=127.0.0.1
REDIS_PORT=6379
REDIS_PASSWORD=
REDIS_DATABASE=0
```

### 2. application.yml
Spring Boot配置文件已更新，从.env读取Redis配置：

```yaml
spring:
  data:
    redis:
      host: ${REDIS_HOST}
      port: ${REDIS_PORT}
      password: ${REDIS_PASSWORD}
      database: ${REDIS_DATABASE}
      timeout: 5000ms
```

### 3. pom.xml
已添加Redisson依赖：

```xml
<dependency>
    <groupId>org.redisson</groupId>
    <artifactId>redisson-spring-boot-starter</artifactId>
    <version>3.27.2</version>
</dependency>
```

## 配置类

### RedisConfig.java
位置: `src/main/java/org/example/config/RedisConfig.java`

该配置类会自动创建 `RedissonClient` Bean，配置包括：
- 单机模式连接
- 连接池大小：64
- 最小空闲连接：10
- 支持密码认证（如果配置了密码）
- 支持超时时间配置

## 使用方式

### 1. 直接注入 RedissonClient

```java
@Service
public class YourService {
    
    @Autowired
    private RedissonClient redissonClient;
    
    public void example() {
        // 字符串操作
        RBucket<String> bucket = redissonClient.getBucket("key");
        bucket.set("value");
        String value = bucket.get();
        
        // Map操作
        RMap<String, String> map = redissonClient.getMap("myMap");
        map.put("key1", "value1");
        
        // 分布式锁
        RLock lock = redissonClient.getLock("myLock");
        try {
            lock.lock();
            // 业务逻辑
        } finally {
            lock.unlock();
        }
    }
}
```

### 2. 使用封装的 RedisService

我已经创建了一个 `RedisService` 类，封装了常用的Redis操作：

```java
@Service
public class YourService {
    
    @Autowired
    private RedisService redisService;
    
    public void example() {
        // 设置值
        redisService.set("key", "value");
        
        // 设置带过期时间的值
        redisService.set("key", "value", 30, TimeUnit.MINUTES);
        
        // 获取值
        Object value = redisService.get("key");
        
        // Hash操作
        redisService.hset("user:1", "name", "张三");
        String name = redisService.hget("user:1", "name");
        
        // 分布式锁
        if (redisService.tryLock("lockKey", 10, 30, TimeUnit.SECONDS)) {
            try {
                // 业务逻辑
            } finally {
                redisService.unlock("lockKey");
            }
        }
    }
}
```

## 常用操作

### 字符串操作
```java
RBucket<String> bucket = redissonClient.getBucket("key");
bucket.set("value");
bucket.set("value", 10, TimeUnit.MINUTES); // 带过期时间
String value = bucket.get();
```

### Hash操作
```java
RMap<String, String> map = redissonClient.getMap("user:1");
map.put("name", "张三");
map.put("age", "25");
String name = map.get("name");
```

### List操作
```java
RList<String> list = redissonClient.getList("myList");
list.add("item1");
list.add("item2");
```

### Set操作
```java
RSet<String> set = redissonClient.getSet("mySet");
set.add("member1");
set.add("member2");
```

### 分布式锁
```java
RLock lock = redissonClient.getLock("myLock");
try {
    // 等待10秒，锁定30秒后自动释放
    if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
        try {
            // 执行业务逻辑
        } finally {
            lock.unlock();
        }
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
}
```

## 启动前准备

1. 确保Redis服务已启动
2. 根据实际情况修改 `.env` 文件中的Redis配置
3. 如果Redis设置了密码，请在 `REDIS_PASSWORD` 中填写

## 启动Redis（如果未启动）

### macOS (使用Homebrew)
```bash
brew services start redis
```

### Docker
```bash
docker run -d -p 6379:6379 --name redis redis:latest
```

## 测试连接

可以在启动日志中看到Redisson的连接信息，如果配置正确，应用会成功连接到Redis服务器。

## 更多功能

Redisson提供了丰富的功能：
- 分布式集合（Map, Set, List, Queue等）
- 分布式锁和同步器
- 分布式服务（Remote Service）
- 分布式对象
- 布隆过滤器
- 限流器
- 等等...

详细文档请参考：https://github.com/redisson/redisson/wiki
