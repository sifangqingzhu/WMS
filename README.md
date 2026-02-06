# WMS 仓库管理系统

## 技术栈

- **Java**: 17 (OpenJDK 17.0.14)
- **Maven**: 3.9.12
- **Spring Boot**: 3.2.2
- **数据库**: PostgreSQL
- **ORM**: MyBatis-Plus 3.5.5
- **缓存**: Redis (Redisson 3.27.2)
- **安全**: JWT + BCrypt
- **API 文档**: SpringDoc OpenAPI (Swagger)

## 环境要求

### 已安装的开发工具
✅ **Java 17** - OpenJDK 17.0.14 (Homebrew)
✅ **Maven 3.9.12** - Apache Maven
✅ **Spring Boot 3.2.2** - 已在 pom.xml 中配置

### Java 环境配置

确保在 `~/.zshrc` 中已添加以下配置（已自动添加）：
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
```

重新加载配置：
```bash
source ~/.zshrc
```

验证安装：
```bash
java -version    # 应显示 17.0.14
mvn -version     # 应显示 Maven 3.9.12 和 Java 17
```

## 项目配置完成

已完成以下配置：

### ✅ 1. 创建 `.env` 文件
已在项目根目录创建 `.env` 文件，包含 PostgreSQL 数据库配置：
- 数据库地址：127.0.0.1
- 数据库端口：
- 数据库名称：wms
- 数据库用户：postgres
- 数据库密码：114514

### ✅ 2. 更新 `pom.xml`
已添加完整的 Spring Boot 3 依赖：
- Spring Boot Web Starter
- MyBatis-Plus Spring Boot 3 Starter (3.5.5)
- PostgreSQL JDBC 驱动
- JWT (JJWT 0.12.5)
- BCrypt 密码加密
- Redisson (Redis 客户端)
- SpringDoc OpenAPI (Swagger UI)
- dotenv-java - 用于读取 .env 配置文件

### ✅ 3. 创建数据库配置类
已创建 `DatabaseConfig.java`，提供数据库连接和配置读取功能

### ✅ 4. 创建 Spring Boot 应用
已创建完整的 Spring Boot 应用结构，包括：
- 用户认证系统 (注册/登录/登出)
- JWT Token 管理
- Redis 缓存服务
- Token 黑名单机制
- RESTful API 控制器

### ✅ 5. 更新 `.gitignore`
已添加 `.env` 到 `.gitignore`，防止敏感信息被提交到版本控制

## 快速开始

### 1. 在 IntelliJ IDEA 中重新加载 Maven 项目
右键点击 `pom.xml` → 选择 **"Maven"** → 点击 **"Reload project"**

或者点击右侧的 Maven 工具栏，然后点击刷新图标 🔄

这将下载所有依赖包。

### 2. 使用 Maven 命令行构建项目
```bash
# 进入项目目录
cd /Users/gaozeran/Desktop/WMS

# 清理并编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目
mvn package

# 运行 Spring Boot 应用
mvn spring-boot:run
```


### 3. 运行 Spring Boot 应用
```bash
# 运行 Spring Boot 应用
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

### 4. 访问 API 文档
启动应用后，访问 Swagger UI：
```
http://localhost:8080/swagger-ui.html
```

## API 端点

### 认证相关 API
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/logout` - 用户登出
- `POST /api/auth/validate-token` - 验证 Token

详细 API 文档请查看 `API.md` 文件或访问 Swagger UI。

## 项目结构
```
wms/
├── .env                              # 环境配置文件（不会被提交到 Git）
├── .gitignore                       # Git 忽略文件配置
├── pom.xml                          # Maven 项目配置文件
├── README.md                        # 项目说明文档
├── API.md                           # API 接口文档
├── init.sql                         # 数据库初始化脚本
└── src/
    └── main/
        ├── java/
        │   └── org/
        │       └── example/
        │           ├── WmsApplication.java              # Spring Boot 主程序
        │           ├── DatabaseConfig.java              # 数据库配置类
        │           ├── config/                          # 配置类
        │           │   ├── MyBatisPlusConfig.java       # MyBatis-Plus 配置
        │           │   ├── MyMetaObjectHandler.java     # 自动填充处理器
        │           │   └── RedisConfig.java             # Redis 配置
        │           ├── controller/                      # 控制器
        │           │   └── AuthController.java          # 认证控制器
        │           ├── dao/                             # 数据访问对象
        │           │   └── UserDao.java
        │           ├── dto/                             # 数据传输对象
        │           │   ├── ApiResponse.java             # 统一响应格式
        │           │   ├── LoginRequest.java            # 登录请求
        │           │   ├── LoginResponse.java           # 登录响应
        │           │   ├── RegisterRequest.java         # 注册请求
        │           │   └── TokenValidateRequest.java    # Token 验证请求
        │           ├── entity/                          # 实体类
        │           │   └── SysUser.java                 # 用户实体
        │           ├── mapper/                          # MyBatis Mapper
        │           │   └── UserMapper.java
        │           ├── repository/                      # 仓储接口
        │           │   └── UserRepository.java
        │           ├── service/                         # 服务层
        │           │   ├── AuthService.java             # 认证服务
        │           │   ├── RedisService.java            # Redis 服务
        │           │   └── TokenBlacklistService.java   # Token 黑名单服务
        │           └── util/                            # 工具类
        │               ├── JwtUtil.java                 # JWT 工具
        │               └── PasswordUtil.java            # 密码加密工具
        └── resources/
            └── application.yml                          # Spring Boot 配置文件
```

## 数据库设置

### 2. 确保 PostgreSQL 数据库已启动并创建数据库
在运行程序之前，请确保：
```bash
# 启动 PostgreSQL 服务（如果还未启动）
# 连接并创建 wms 数据库
psql -U postgres -h 127.0.0.1 -p 7301 -c "CREATE DATABASE wms;"

# 或者使用 init.sql 初始化数据库表
psql -U postgres -h 127.0.0.1 -p 7301 -d wms -f init.sql
```

### 数据库表结构
主要数据表：
- `sys_user` - 系统用户表（包含用户名、密码、角色等信息）
- 其他表结构请参考 `init.sql` 文件

## 安全提示
⚠️ **重要**: `.env` 文件包含敏感信息（数据库密码），已被添加到 `.gitignore` 中。
请勿将此文件提交到版本控制系统！

## 开发工具建议

### IntelliJ IDEA 插件推荐
- **Lombok** - 简化 Java 代码
- **MyBatis X** - MyBatis 增强工具
- **Spring Boot Assistant** - Spring Boot 开发助手
- **Redis** - Redis 客户端

### Maven 常用命令
```bash
mvn clean                # 清理项目
mvn compile              # 编译项目
mvn test                 # 运行测试
mvn package              # 打包项目
mvn spring-boot:run      # 运行 Spring Boot 应用
mvn dependency:tree      # 查看依赖树
```

## 故障排查

### 如果遇到 "数据库连接失败"
1. PostgreSQL 服务是否已启动
2. `.env` 文件中的配置是否正确
3. 数据库 'wms' 是否已创建
4. 端口 7301 是否被占用或配置正确

### 如果遇到 "Maven 下载依赖失败"
1. 检查网络连接
2. 如果在中国，可以配置阿里云 Maven 镜像：
   编辑 `~/.m2/settings.xml`，添加：
   ```xml
   <mirrors>
     <mirror>
       <id>aliyun</id>
       <mirrorOf>central</mirrorOf>
       <name>Aliyun Maven</name>
       <url>https://maven.aliyun.com/repository/public</url>
     </mirror>
   </mirrors>
   ```

### 如果遇到 "Java 版本不匹配"
1. 确保使用 Java 17：`java -version`
2. 检查 JAVA_HOME 环境变量：`echo $JAVA_HOME`
3. 重新加载配置：`source ~/.zshrc`

## 文档参考
- [API 接口文档](API.md)
- [Redis 配置说明](REDIS_CONFIG.md)
- [Token 黑名单机制](TOKEN_BLACKLIST.md)

## 联系方式
如有问题，请查看项目文档或提交 Issue。


