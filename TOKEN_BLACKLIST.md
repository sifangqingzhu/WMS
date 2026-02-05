# Token黑名单机制说明

## 功能概述

已成功实现基于Redis的Token黑名单机制，在用户退出登录后，将Token存入Redis黑名单，防止Token被重复使用。

## 实现原理

1. **用户登出时**：将Token作为Key存入Redis，过期时间与Token原本的过期时间对齐
2. **验证Token时**：先检查Token是否在黑名单中，如果在则拒绝访问
3. **自动清理**：Redis会在Token过期时自动删除黑名单记录，无需手动清理

## 新增文件

### 1. TokenBlacklistService.java
位置：`src/main/java/org/example/service/TokenBlacklistService.java`

**核心方法**：
- `addToBlacklist(token, expirationDate)` - 将Token加入黑名单
- `isBlacklisted(token)` - 检查Token是否在黑名单中
- `removeFromBlacklist(token)` - 从黑名单移除Token（可选）

**Redis Key格式**：`token:blacklist:{token}`

## 修改文件

### 1. JwtUtil.java
新增方法：
```java
public static Date getExpirationDate(String token)
```
用于获取Token的过期时间，以便设置Redis的TTL。

### 2. AuthService.java
- **新增依赖注入**：`TokenBlacklistService`
- **修改 `validateToken()` 方法**：添加黑名单检查
- **新增 `logout()` 方法**：处理用户登出逻辑

### 3. AuthController.java
新增接口：
```java
POST /api/auth/logout
```

## API接口

### 用户登出
**接口**: `POST /api/auth/logout`

**请求头**:
```
Authorization: Bearer {token}
```

**成功响应**:
```json
{
  "code": 200,
  "message": "登出成功",
  "data": null,
  "timestamp": "2026-02-05T18:50:00"
}
```

**失败响应**:
```json
{
  "code": 401,
  "message": "未提供认证Token",
  "data": null,
  "timestamp": "2026-02-05T18:50:00"
}
```

## 使用流程

### 1. 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

响应获得Token：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "testuser"
  }
}
```

### 2. 使用Token访问接口
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 3. 用户登出
```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 4. 再次使用该Token将被拒绝
```bash
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

响应：
```json
{
  "code": 401,
  "message": "Token无效或已过期",
  "data": null
}
```

## 工作流程图

```
用户登出
    ↓
提取Token
    ↓
验证Token有效性
    ↓
获取Token过期时间
    ↓
计算剩余有效期 (TTL)
    ↓
存入Redis黑名单
Key: token:blacklist:{token}
Value: "logout"
TTL: Token剩余有效期
    ↓
返回登出成功

--------------------

后续请求
    ↓
提取Token
    ↓
检查是否在黑名单
    ↓
是 → 拒绝访问（401）
否 → 继续验证Token
```

## Redis存储示例

### 登出后Redis中的数据
```
Key: token:blacklist:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Value: logout
TTL: 604800秒 (7天，与Token过期时间一致)
```

### 查看Redis中的黑名单
```bash
# 连接Redis
redis-cli

# 查看所有黑名单Token
KEYS token:blacklist:*

# 查看某个Token的TTL（剩余过期时间）
TTL token:blacklist:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...

# 查看某个Token是否在黑名单中
EXISTS token:blacklist:eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 优势特点

1. **安全性高**：用户登出后Token立即失效，无法被重复使用
2. **性能优秀**：使用Redis内存存储，查询速度极快
3. **自动清理**：利用Redis的TTL机制自动清理过期记录
4. **精确对齐**：黑名单过期时间与Token过期时间完全对齐，不浪费存储空间
5. **容错处理**：检查黑名单失败时，为安全起见会拒绝访问

## 测试建议

### 1. 测试正常登出
```bash
# 1. 登录获取Token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}' \
  | jq -r '.data.token')

# 2. 验证Token有效
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"

# 3. 登出
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer $TOKEN"

# 4. 再次验证（应该失败）
curl -X GET http://localhost:8080/api/auth/me \
  -H "Authorization: Bearer $TOKEN"
```

### 2. 测试重复登出
```bash
# 使用已登出的Token再次登出（应该失败）
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

### 3. 测试无效Token
```bash
# 使用无效Token登出（应该失败）
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer invalid_token"
```

## 注意事项

1. **确保Redis服务运行**：黑名单功能依赖Redis，请确保Redis服务正常运行
2. **Token安全**：请妥善保管Token，不要在客户端长期存储
3. **多端登录**：如果需要支持多端登录，每个端应该有独立的Token
4. **Token刷新**：如果实现Token刷新机制，记得将旧Token加入黑名单

## 扩展功能建议

1. **踢人功能**：管理员可以将指定用户的所有Token加入黑名单
2. **单点登录**：同一用户只能有一个有效Token，新登录时旧Token自动失效
3. **Token刷新**：实现Token刷新机制，延长用户会话时间
4. **黑名单统计**：记录黑名单Token数量、登出用户数等统计信息

---

**实现完成时间**: 2026-02-05
**技术栈**: Spring Boot + Redisson + Redis + JWT
