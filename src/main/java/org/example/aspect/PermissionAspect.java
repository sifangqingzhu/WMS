package org.example.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.annotation.RequirePermission;
import org.example.service.MenuService;
import org.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 权限验证切面
 * 拦截带有 @RequirePermission 注解的方法，验证用户权限
 */
@Aspect
@Component
public class PermissionAspect {

    private static final Logger log = LoggerFactory.getLogger(PermissionAspect.class);

    private final MenuService menuService;

    public PermissionAspect(MenuService menuService) {
        this.menuService = menuService;
    }

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        String permissionCode = requirePermission.value();

        log.debug("权限验证: {}", permissionCode);

        // 1. 获取当前请求
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new RuntimeException("无法获取请求上下文");
        }
        HttpServletRequest request = attributes.getRequest();

        // 2. 从请求头获取Token
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("未登录或Token无效");
        }
        token = token.substring(7);

        // 3. 从Token中提取用户ID
        String userId;
        try {
            userId = JwtUtil.getUserId(token);
        } catch (Exception e) {
            throw new RuntimeException("Token解析失败: " + e.getMessage());
        }

        // 4. 检查用户是否拥有该权限
        boolean hasPermission = menuService.hasPermission(userId, permissionCode);

        if (!hasPermission) {
            log.warn("用户 {} 无权限访问: {}", userId, permissionCode);
            throw new RuntimeException("权限不足: 需要 " + permissionCode + " 权限");
        }

        log.debug("用户 {} 权限验证通过: {}", userId, permissionCode);

        // 5. 继续执行方法
        return joinPoint.proceed();
    }
}
