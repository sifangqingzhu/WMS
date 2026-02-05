package org.example;

import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * WMS 仓库管理系统 - Spring Boot 应用入口
 * 使用构造器注入，完全解耦
 */
@SpringBootApplication
public class WmsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(WmsApplication.class);

    private final UserRepository userRepository;
    private final AuthService authService;

    // 构造器注入
    public WmsApplication(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("=== WMS 仓库管理系统启动 ===");

        log.info("数据库表结构请手动执行 init.sql 初始化");

        // 创建默认管理员账户
        log.info("检查默认管理员账户...");
        if (!userRepository.existsByUsername("admin")) {
            boolean created = authService.register("admin", "admin123", "系统管理员", "admin@wms.com", "13800138000");
            if (created) {
                log.info("✓ 默认管理员账户创建成功");
                log.info("  用户名: admin");
                log.info("  密码: admin123");
            } else {
                log.error("✗ 默认管理员账户创建失败");
            }
        } else {
            log.info("✓ 管理员账户已存在");
        }

        log.info("================================");
        log.info("🚀 WMS API 服务器已启动！");
        log.info("服务地址: http://localhost:8080");
        log.info("Swagger文档: http://localhost:8080/swagger-ui.html");
        log.info("API文档: http://localhost:8080/v3/api-docs");
        log.info("================================");
        log.info("可用的 API 接口：");
        log.info("  POST   /api/auth/login      - 用户登录");
        log.info("  POST   /api/auth/register   - 用户注册");
        log.info("  POST   /api/auth/validate   - 验证 Token");
        log.info("  GET    /api/auth/me         - 获取当前用户信息");
        log.info("================================");
    }
}
