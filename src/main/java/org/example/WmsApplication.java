package org.example;

import org.example.repository.UserRepository;
import org.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * WMS 仓库管理系统 - Spring Boot 应用入口
 * 使用构造器注入，完全解耦
 */
@SpringBootApplication
public class WmsApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(WmsApplication.class);

    private final UserRepository userRepository;
    private final AuthService authService;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    // 构造器注入
    public WmsApplication(UserRepository userRepository,
                          AuthService authService,
                          RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
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

        // 动态获取并输出所有API接口
        printAllApiEndpoints();

        log.info("================================");
    }

    /**
     * 动态获取并打印所有API接口
     */
    private void printAllApiEndpoints() {
        log.info("可用的 API 接口：");

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
        List<String> endpoints = new ArrayList<>();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo mappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();

            // 只显示org.example包下的接口
            String controllerClass = handlerMethod.getBeanType().getName();
            if (!controllerClass.startsWith("org.example")) {
                continue;
            }

            // 获取请求方法
            String methods = mappingInfo.getMethodsCondition().getMethods().toString();
            if (methods.equals("[]")) {
                methods = "ALL";
            } else {
                methods = methods.replace("[", "").replace("]", "");
            }

            // 获取路径
            String paths = mappingInfo.getPathPatternsCondition() != null
                    ? mappingInfo.getPathPatternsCondition().getPatterns().toString()
                    : mappingInfo.getPatternsCondition() != null
                    ? mappingInfo.getPatternsCondition().getPatterns().toString()
                    : "[]";
            paths = paths.replace("[", "").replace("]", "");

            // 获取方法名作为描述
            String methodName = handlerMethod.getMethod().getName();

            endpoints.add(String.format("  %-6s %-35s - %s", methods, paths, methodName));
        }

        // 排序后输出
        endpoints.sort(Comparator.naturalOrder());
        for (String endpoint : endpoints) {
            log.info(endpoint);
        }

        log.info("共 {} 个接口", endpoints.size());
    }
}
