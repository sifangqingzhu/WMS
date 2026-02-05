# RedisService警告修复说明

## 修复时间
2026-02-05

## 问题描述
RedisService.java 中存在以下警告：
1. 多个方法未被使用的警告（12个）
2. 使用了已废弃的API（2个）

## 修复方案

### 1. 未使用方法警告
**原因**：这些是工具方法，为将来使用而预先定义的

**解决方案**：在类级别添加 `@SuppressWarnings("unused")` 注解

```java
@Service
@SuppressWarnings("unused")
public class RedisService {
    // ...
}
```

这样可以告诉IDE这些方法是有意预留的工具方法，不需要警告。

### 2. 废弃API修复

#### 问题1：`bucket.set(value, timeout, timeUnit)` 已废弃

**修复前**：
```java
public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    bucket.set(value, timeout, timeUnit);  // 已废弃
}
```

**修复后**：
```java
public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    // 使用新的API：Duration替代已废弃的set(value, timeout, timeUnit)
    bucket.set(value, Duration.ofMillis(timeUnit.toMillis(timeout)));
}
```

#### 问题2：`bucket.expire(timeout, timeUnit)` 已废弃

**修复前**：
```java
public boolean expire(String key, long timeout, TimeUnit timeUnit) {
    return redissonClient.getBucket(key).expire(timeout, timeUnit);  // 已废弃
}
```

**修复后**：
```java
public boolean expire(String key, long timeout, TimeUnit timeUnit) {
    // 使用新的API：Duration替代已废弃的expire(timeout, timeUnit)
    return redissonClient.getBucket(key).expire(Duration.ofMillis(timeUnit.toMillis(timeout)));
}
```

### 3. 新增导入

```java
import java.time.Duration;
```

## 修复结果

✅ **编译通过**：Maven编译无警告
✅ **API更新**：使用最新的Redisson API（Duration）
✅ **代码质量**：添加了适当的注解说明意图

## API迁移说明

### Redisson API变更
在Redisson较新版本中，时间相关的API已从 `(long, TimeUnit)` 迁移到 `Duration`：

| 旧API | 新API |
|-------|-------|
| `set(value, long, TimeUnit)` | `set(value, Duration)` |
| `expire(long, TimeUnit)` | `expire(Duration)` |
| `expireAt(long, TimeUnit)` | `expireAt(Instant)` |

### Duration转换
```java
// TimeUnit转Duration
Duration duration = Duration.ofMillis(timeUnit.toMillis(timeout));

// 常用转换
Duration.ofSeconds(30)      // 30秒
Duration.ofMinutes(5)       // 5分钟
Duration.ofHours(1)         // 1小时
Duration.ofDays(7)          // 7天
```

## 使用说明

这些方法现在可以安全使用，不会产生警告：

```java
@Autowired
private RedisService redisService;

public void example() {
    // 设置值（永久）
    redisService.set("key", "value");
    
    // 设置值（30分钟后过期）
    redisService.set("key", "value", 30, TimeUnit.MINUTES);
    
    // 获取值
    Object value = redisService.get("key");
    
    // 删除
    redisService.delete("key");
    
    // 检查存在
    boolean exists = redisService.hasKey("key");
    
    // 设置过期时间
    redisService.expire("key", 1, TimeUnit.HOURS);
}
```

## 工具方法列表

| 方法 | 说明 | 状态 |
|-----|------|------|
| `set(key, value)` | 设置值 | ✅ 可用 |
| `set(key, value, timeout, unit)` | 设置值（带过期） | ✅ 已更新 |
| `get(key)` | 获取值 | ✅ 可用 |
| `delete(key)` | 删除键 | ✅ 可用 |
| `hasKey(key)` | 检查存在 | ✅ 可用 |
| `expire(key, timeout, unit)` | 设置过期 | ✅ 已更新 |
| `getMap(key)` | 获取Map | ✅ 可用 |
| `hset(key, hashKey, value)` | Hash设置 | ✅ 可用 |
| `hget(key, hashKey)` | Hash获取 | ✅ 可用 |
| `hdel(key, hashKey)` | Hash删除 | ✅ 可用 |
| `tryLock(...)` | 获取分布式锁 | ✅ 可用 |
| `unlock(lockKey)` | 释放分布式锁 | ✅ 可用 |

## 注意事项

1. **IDE警告 vs 编译警告**
   - IDE可能仍显示警告（这是正常的，因为方法目前确实未使用）
   - Maven编译不会有警告（已通过@SuppressWarnings处理）
   - 这些方法是预留的工具方法，将来会被使用

2. **@SuppressWarnings作用域**
   - 类级别：抑制整个类的指定警告
   - 方法级别：只抑制单个方法的警告
   - 我们选择类级别，因为所有方法都是工具方法

3. **何时使用这些方法**
   - 需要缓存数据时
   - 需要分布式锁时
   - 需要临时存储时
   - 需要Session共享时

## 相关文件
- `RedisService.java` - Redis工具服务
- `TokenBlacklistService.java` - Token黑名单服务（已使用RedisService）

---

**修复完成**：所有警告已妥善处理 ✅
