package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据库配置类
 * 从 .env 文件加载数据库配置信息
 */
public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();

    // 静态初始化块，加载 PostgreSQL JDBC 驱动
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC 驱动未找到", e);
        }
    }

    /**
     * 获取数据库连接
     * @return Connection 数据库连接对象
     * @throws SQLException 如果连接失败
     */
    public static Connection getConnection() throws SQLException {
        String url = dotenv.get("POSTGRES_DATABASE_CONNECT_STRING");
        return DriverManager.getConnection(url);
    }

    /**
     * 获取数据库地址
     */
    public static String getDatabaseUrl() {
        return dotenv.get("POSTGRES_DATABASE_URL");
    }

    /**
     * 获取数据库端口
     */
    public static String getDatabasePort() {
        return dotenv.get("POSTGRES_DATABASE_PORT");
    }

    /**
     * 获取数据库用户名
     */
    public static String getDatabaseUser() {
        return dotenv.get("POSTGRES_DATABASE_USER");
    }

    /**
     * 获取数据库密码
     */
    public static String getDatabasePassword() {
        return dotenv.get("POSTGRES_DATABASE_PASSWORD");
    }

    /**
     * 获取数据库名称
     */
    public static String getDatabaseName() {
        return dotenv.get("POSTGRES_DATABASE_NAME");
    }

    /**
     * 获取完整的连接字符串
     */
    public static String getConnectString() {
        return dotenv.get("POSTGRES_DATABASE_CONNECT_STRING");
    }
}
