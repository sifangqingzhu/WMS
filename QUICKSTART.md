# WMS 快速启动指南

## ✅ 环境检查清单

在启动项目之前，请确保以下内容已完成：

- [ ] Java 17 已安装 (`java -version` 应显示 17.0.14)
- [ ] Maven 3.9.12 已安装 (`mvn -version` 应显示 3.9.12)
- [ ] JAVA_HOME 环境变量已配置
- [ ] PostgreSQL 数据库已启动（端口 7301）
- [ ] Redis 服务已启动（可选，如果使用缓存）
- [ ] `.env` 文件已创建并配置

## 快速启动步骤

### 方式一：使用 Maven 命令行

```bash
# 1. 进入项目目录
cd /Users/gaozeran/Desktop/WMS

# 2. 设置环境变量（如果还没有重启终端）
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# 3. 清理并编译项目（第一次运行）
mvn clean compile

# 4. 启动应用
mvn spring-boot:run
```

### 方式二：在 IntelliJ IDEA 中启动

1. **打开项目**
   - File → Open → 选择 `/Users/gaozeran/Desktop/WMS`

2. **重新加载 Maven 项目**
   - 右键点击 `pom.xml`
   - 选择 "Maven" → "Reload project"

3. **运行应用**
   - 找到 `src/main/java/org/example/WmsApplication.java`
   - 点击左侧的绿色运行按钮 ▶️
   - 或按 `Shift + F10`

## 验证应用是否启动成功

### 1. 查看终端输出
应该看到类似以下信息：
```
Started WmsApplication in X.XXX seconds
```

### 2. 访问应用
打开浏览器访问：
- 应用首页：http://localhost:8080
- API 文档：http://localhost:8080/swagger-ui.html

### 3. 测试 API
使用 curl 或 Postman 测试：
```bash
# 健康检查（如果有）
curl http://localhost:8080/actuator/health

# 注册新用户
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'
```

## 常用 Maven 命令

```bash
# 清理项目
mvn clean

# 编译项目
mvn compile

# 运行测试
mvn test

# 打包项目（生成 JAR 文件）
mvn package

# 跳过测试打包
mvn package -DskipTests

# 运行 Spring Boot 应用
mvn spring-boot:run

# 查看依赖树
mvn dependency:tree

# 更新依赖
mvn clean install -U
```

## 停止应用

### Maven 命令行方式
按 `Ctrl + C` 停止应用

### IntelliJ IDEA 方式
点击运行窗口中的红色停止按钮 ⏹

## 运行模式

### 开发模式（默认）
```bash
mvn spring-boot:run
```

### 生产模式
```bash
# 1. 打包应用
mvn clean package -DskipTests

# 2. 运行 JAR 文件
java -jar target/wms-1.0-SNAPSHOT.jar
```

### 指定配置文件
```bash
# 使用特定配置文件
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# 或者
java -jar target/wms-1.0-SNAPSHOT.jar --spring.profiles.active=prod
```

## 调试模式

### Maven 调试
```bash
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### IntelliJ IDEA 调试
1. 点击 Debug 按钮 🐞（而不是 Run 按钮）
2. 或按 `Shift + F9`
3. 设置断点后开始调试

## 环境变量配置

### 临时设置（当前终端会话）
```bash
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"
```

### 永久设置（添加到 ~/.zshrc）
```bash
# 编辑配置文件
nano ~/.zshrc

# 添加以下内容
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# 保存后重新加载
source ~/.zshrc
```

## 日志查看

### 控制台日志
启动应用后，日志会实时显示在终端中

### 日志文件
查看日志文件：
```bash
# 查看最新日志
tail -f logs/wms.log

# 查看全部日志
cat logs/wms.log

# 查看最后 100 行
tail -n 100 logs/wms.log
```

## 故障排查

### 问题 1: 端口 8080 已被占用
**错误信息：** `Port 8080 was already in use`

**解决方案：**
```bash
# 查看占用端口的进程
lsof -i :8080

# 终止进程（替换 PID）
kill -9 <PID>

# 或者修改应用端口（编辑 application.yml）
server:
  port: 8081
```

### 问题 2: 无法连接数据库
**错误信息：** `Connection refused` 或 `Could not connect to database`

**解决方案：**
1. 检查 PostgreSQL 是否启动
2. 检查 `.env` 文件配置
3. 检查数据库是否存在

### 问题 3: Maven 构建失败
**错误信息：** `Failed to execute goal`

**解决方案：**
```bash
# 清理并重新构建
mvn clean install -U

# 或使用离线模式（如果依赖已下载）
mvn clean install -o
```

### 问题 4: OutOfMemoryError
**解决方案：** 增加 JVM 内存
```bash
# Maven 方式
export MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=512m"
mvn spring-boot:run

# JAR 方式
java -Xmx1024m -jar target/wms-1.0-SNAPSHOT.jar
```

## 性能优化

### 加快启动速度
```bash
# 跳过测试
mvn spring-boot:run -DskipTests

# 使用离线模式（依赖已下载）
mvn spring-boot:run -o
```

### 热重载（开发时）
在 `pom.xml` 中添加 Spring Boot DevTools：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

## 下一步

启动成功后：
1. 访问 Swagger UI 查看 API 文档
2. 使用 Postman 测试 API
3. 查看 `API.md` 了解详细的 API 使用方法
4. 阅读 `README.md` 了解项目架构

---

**快速提示：**
- 首次启动可能需要几分钟下载依赖
- 确保数据库已创建并可连接
- 检查防火墙设置，确保端口未被阻止
- 使用 `mvn clean` 清理旧的构建文件

祝你开发愉快！🚀
