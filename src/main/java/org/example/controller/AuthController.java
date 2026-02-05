package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.dto.*;
import org.example.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 * 使用构造器注入，完全解耦
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证接口", description = "用户登录、注册、Token验证等接口")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    // 构造器注入
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "验证用户名和密码，返回JWT Token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        log.info("收到登录请求: username={}", request.getUsername());

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            log.warn("登录请求失败: 用户名为空");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "用户名不能为空"));
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("登录请求失败: 密码为空");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "密码不能为空"));
        }

        LoginResponse loginResponse = authService.login(request);

        if (loginResponse.isSuccess()) {
            log.info("登录成功: username={}, userId={}", request.getUsername(), loginResponse.getUserId());
            return ResponseEntity.ok(ApiResponse.success("登录成功", loginResponse));
        } else {
            log.warn("登录失败: username={}, reason={}", request.getUsername(), loginResponse.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, loginResponse.getMessage()));
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "创建新用户账户")
    public ResponseEntity<ApiResponse<Void>> register(@RequestBody RegisterRequest request) {
        log.info("收到注册请求: username={}, realName={}, email={}",
                request.getUsername(), request.getRealName(), request.getEmail());

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            log.warn("注册请求失败: 用户名为空");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "用户名不能为空"));
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            log.warn("注册请求失败: 密码为空");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "密码不能为空"));
        }

        boolean success = authService.register(
                request.getUsername(),
                request.getPassword(),
                request.getRealName(),
                request.getEmail(),
                request.getPhone()
        );

        if (success) {
            log.info("注册成功: username={}", request.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("注册成功", null));
        } else {
            log.warn("注册失败: username={}, 用户名可能已存在", request.getUsername());
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "注册失败，用户名可能已存在"));
        }
    }

    /**
     * 验证 Token
     */
    @PostMapping("/validate")
    @Operation(summary = "验证Token", description = "验证JWT Token是否有效")
    public ResponseEntity<ApiResponse<TokenValidateResponse>> validateToken(@RequestBody TokenValidateRequest request) {
        log.info("收到Token验证请求");

        if (request.getToken() == null || request.getToken().trim().isEmpty()) {
            log.warn("Token验证请求失败: Token为空");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "Token不能为空"));
        }

        boolean isValid = authService.validateToken(request.getToken());

        if (isValid) {
            String username = authService.getUsernameFromToken(request.getToken());
            log.info("Token验证成功: username={}", username);
            return ResponseEntity.ok(ApiResponse.success("Token有效",
                    new TokenValidateResponse(true, username)));
        } else {
            log.warn("Token验证失败: Token无效或已过期");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token无效或已过期"));
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    @Operation(summary = "获取当前用户", description = "根据Token获取当前登录用户信息")
    public ResponseEntity<ApiResponse<CurrentUserResponse>> getCurrentUser(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        log.info("收到获取当前用户请求");

        if (authHeader == null || authHeader.trim().isEmpty()) {
            log.warn("获取当前用户失败: 未提供Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "未提供认证Token"));
        }

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        boolean isValid = authService.validateToken(token);

        if (isValid) {
            String username = authService.getUsernameFromToken(token);
            log.info("获取当前用户成功: username={}", username);
            return ResponseEntity.ok(ApiResponse.success("获取成功",
                    new CurrentUserResponse(username)));
        } else {
            log.warn("获取当前用户失败: Token无效或已过期");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "Token无效或已过期"));
        }
    }

    /**
     * 用户登出
     */
    @PostMapping("/logout")
    @Operation(summary = "用户登出", description = "使当前Token失效，加入黑名单")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        log.info("收到登出请求");

        if (authHeader == null || authHeader.trim().isEmpty()) {
            log.warn("登出请求失败: 未提供Authorization header");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "未提供认证Token"));
        }

        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        boolean success = authService.logout(token);

        if (success) {
            log.info("登出成功");
            return ResponseEntity.ok(ApiResponse.success("登出成功", null));
        } else {
            log.warn("登出失败: Token无效");
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(400, "登出失败，Token无效"));
        }
    }

    /**
     * Token 验证响应
     */
    public static class TokenValidateResponse {
        private boolean valid;
        private String username;

        public TokenValidateResponse(boolean valid, String username) {
            this.valid = valid;
            this.username = username;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    /**
     * 当前用户响应
     */
    public static class CurrentUserResponse {
        private String username;

        public CurrentUserResponse(String username) {
            this.username = username;
        }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}
