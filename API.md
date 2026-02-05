# WMS API 文档

## 基础信息
- **Base URL**: `http://localhost:8080`
- **框架**: Spring Boot 3.2.2 + MyBatis-Plus 3.5.5
- **数据库**: PostgreSQL 16

## 认证接口

### 1. 用户登录
**POST** `/api/auth/login`

**请求体**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**成功响应** (200):
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "success": true,
    "token": "eyJhbGciOiJIUzUxMiJ9...",
    "userId": 1,
    "username": "admin",
    "realName": "系统管理员"
  },
  "timestamp": 1738638010000
}
```

### 2. 用户注册
**POST** `/api/auth/register`

**请求体**:
```json
{
  "username": "testuser",
  "password": "password123",
  "realName": "测试用户",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

**成功响应** (201):
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null,
  "timestamp": 1738638010000
}
```

### 3. 验证 Token
**POST** `/api/auth/validate`

**请求体**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### 4. 获取当前用户
**GET** `/api/auth/me`

**请求头**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

## 默认账户
- 用户名: `admin`
- 密码: `admin123`

## 文档地址
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs
