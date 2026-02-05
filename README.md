# WMS 仓库管理系统

## 项目配置完成

已完成以下配置：

### ✅ 1. 创建 `.env` 文件
已在项目根目录创建 `.env` 文件，包含 PostgreSQL 数据库配置：
- 数据库地址：127.0.0.1
- 数据库端口：7301
- 数据库名称：wms
- 数据库用户：postgres
- 数据库密码：114514

### ✅ 2. 更新 `pom.xml`
已添加以下依赖：
- PostgreSQL JDBC 驱动 (42.7.1)
- dotenv-java (3.0.0) - 用于读取 .env 配置文件

### ✅ 3. 创建数据库配置类
已创建 `DatabaseConfig.java`，提供数据库连接和配置读取功能

### ✅ 4. 更新 Main.java
已更新主程序以测试数据库连接

### ✅ 5. 更新 `.gitignore`
已添加 `.env` 到 `.gitignore`，防止敏感信息被提交到版本控制

## 下一步操作

### 1. 在 IntelliJ IDEA 中重新加载 Maven 项目
右键点击 `pom.xml` → 选择 **"Maven"** → 点击 **"Reload project"**

或者点击右侧的 Maven 工具栏，然后点击刷新图标 🔄

这将下载所有依赖包（PostgreSQL JDBC 驱动和 dotenv-java）。

### 2. 确保 PostgreSQL 数据库已启动并创建数据库
在运行程序之前，请确保：
```bash
# 启动 PostgreSQL 服务（如果还未启动）
# 创建 wms 数据库
psql -U postgres -h 127.0.0.1 -p 7301 -c "CREATE DATABASE wms;"
```

### 3. 运行程序
在 IntelliJ IDEA 中：
- 打开 `Main.java`
- 点击左侧的绿色运行按钮 ▶️
- 或者按快捷键 `Shift + F10`

### 4. 预期输出
如果一切配置正确，你应该看到：
```
=== WMS 仓库管理系统 ===
正在连接数据库...
✓ 数据库连接成功！
数据库地址: 127.0.0.1
数据库端口: 7301
数据库名称: wms
数据库用户: postgres
✓ 数据库连接已关闭
```

## 文件结构
```
wms/
├── .env                    # 环境配置文件（不会被提交到 Git）
├── .gitignore             # Git 忽略文件配置
├── pom.xml                # Maven 项目配置文件
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    ├── Main.java              # 主程序
                    └── DatabaseConfig.java    # 数据库配置类
```

## 安全提示
⚠️ **重要**: `.env` 文件包含敏感信息（数据库密码），已被添加到 `.gitignore` 中。
请勿将此文件提交到版本控制系统！

## 故障排查

如果遇到 "数据库连接失败"，请检查：
1. PostgreSQL 服务是否已启动
2. `.env` 文件中的配置是否正确
3. 数据库 'wms' 是否已创建
4. 端口 7301 是否被占用或配置正确
