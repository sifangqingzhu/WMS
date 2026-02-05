# WMS 项目安装指南

本指南将帮助你在 macOS 上安装 WMS 项目所需的所有环境和工具。

## 系统要求
- macOS 10.15 或更高版本
- Homebrew 包管理器
- 至少 2GB 可用磁盘空间

## 安装步骤

### 1. 安装 Homebrew（如果尚未安装）
```bash
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

### 2. 安装 Java 17
```bash
# 安装 OpenJDK 17
brew install openjdk@17

# 创建系统级符号链接
sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
```

### 3. 配置 Java 环境变量
编辑 `~/.zshrc` 文件，添加以下内容：
```bash
# 打开配置文件
nano ~/.zshrc

# 添加以下两行
export JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"

# 保存并退出（Ctrl + O, Enter, Ctrl + X）
```

重新加载配置：
```bash
source ~/.zshrc
```

### 4. 安装 Maven
```bash
# 安装 Maven
brew install maven
```

### 5. 验证安装
```bash
# 验证 Java 版本
java -version
# 应该显示：openjdk version "17.0.14"

# 验证 Maven 版本
mvn -version
# 应该显示：Apache Maven 3.9.12 和 Java version: 17.0.14
```

## 安装结果确认

如果一切正常，你应该看到类似以下输出：

### Java 版本
```
openjdk version "17.0.14" 2025-01-21
OpenJDK Runtime Environment Homebrew (build 17.0.14+0)
OpenJDK 64-Bit Server VM Homebrew (build 17.0.14+0, mixed mode, sharing)
```

### Maven 版本
```
Apache Maven 3.9.12 (848fbb4bf2d427b72bdb2471c22fced7ebd9a7a1)
Maven home: /opt/homebrew/Cellar/maven/3.9.12/libexec
Java version: 17.0.14, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk@17/17.0.14/libexec/openjdk.jdk/Contents/Home
```

## 安装其他依赖（可选）

### 安装 PostgreSQL（如果需要本地数据库）
```bash
brew install postgresql@15
brew services start postgresql@15
```

### 安装 Redis（如果需要本地 Redis）
```bash
brew install redis
brew services start redis
```

### 安装 Git（如果尚未安装）
```bash
brew install git
```

## 项目初始化

### 1. 进入项目目录
```bash
cd /Users/gaozeran/Desktop/WMS
```

### 2. 构建项目
```bash
# 清理并编译项目
mvn clean compile

# 或者直接运行
mvn spring-boot:run
```

### 3. 在 IntelliJ IDEA 中打开项目
1. 打开 IntelliJ IDEA
2. 选择 "Open" 或 "Open or Import"
3. 选择项目根目录（/Users/gaozeran/Desktop/WMS）
4. 等待 IDE 自动下载依赖

## 常见问题解决

### 问题 1: Maven 使用错误的 Java 版本
**解决方案：** 确保 JAVA_HOME 环境变量设置正确
```bash
echo $JAVA_HOME
# 应该输出：/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home
```

### 问题 2: Maven 下载依赖很慢
**解决方案：** 配置国内镜像源
```bash
# 创建或编辑 Maven 配置文件
mkdir -p ~/.m2
nano ~/.m2/settings.xml
```

添加以下内容：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
          http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <mirrors>
        <mirror>
            <id>aliyun-public</id>
            <mirrorOf>central</mirrorOf>
            <name>Aliyun Maven</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>
</settings>
```

### 问题 3: Homebrew 安装很慢
**解决方案：** 使用代理或配置国内源
```bash
# 如果有代理，可以设置：
export https_proxy=http://127.0.0.1:7890
export http_proxy=http://127.0.0.1:7890
```

### 问题 4: 端口被占用
**解决方案：** 修改 Spring Boot 应用端口
编辑 `src/main/resources/application.yml`：
```yaml
server:
  port: 8081  # 改为其他端口
```

## IDE 配置建议

### IntelliJ IDEA 设置
1. **设置项目 SDK**
   - File → Project Structure → Project
   - 设置 SDK 为 Java 17
   - 设置 Language level 为 17

2. **启用注解处理**
   - Preferences → Build, Execution, Deployment → Compiler → Annotation Processors
   - 勾选 "Enable annotation processing"

3. **配置 Maven**
   - Preferences → Build, Execution, Deployment → Build Tools → Maven
   - Maven home directory: /opt/homebrew/Cellar/maven/3.9.12/libexec
   - User settings file: ~/.m2/settings.xml

## 下一步

安装完成后，请参考主 README.md 文件了解如何：
- 配置数据库连接
- 启动应用
- 使用 API
- 查看 API 文档

## 更新环境

### 更新 Java
```bash
brew upgrade openjdk@17
```

### 更新 Maven
```bash
brew upgrade maven
```

### 更新所有 Homebrew 包
```bash
brew update
brew upgrade
```

---

**提示：** 如果遇到问题，请确保：
1. 所有命令都在终端中正确执行
2. 环境变量已正确设置
3. Homebrew 已正常安装
4. 网络连接正常

如需帮助，请查看项目文档或联系开发团队。
