package org.example.service;

import org.example.repository.UserRepository;
import org.example.dto.LoginRequest;
import org.example.dto.LoginResponse;
import org.example.entity.SysUser;
import org.example.util.JwtUtil;
import org.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * 认证服务类
 * 使用构造器注入，完全解耦
 */
@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final TokenBlacklistService tokenBlacklistService;

    // 构造器注入
    public AuthService(UserRepository userRepository, TokenBlacklistService tokenBlacklistService) {
        this.userRepository = userRepository;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest request) {
        log.info("========== 用户登录请求开始 ==========");
        log.info("用户登录请求: username={}", request.getUsername());
        log.debug("DEBUG日志测试: 登录方法被调用");

        try {
            log.debug("DEBUG: 开始查询用户, username={}", request.getUsername());
            // 通过 Repository 查询用户
            SysUser user = userRepository.findByUsername(request.getUsername());

            if (user == null) {
                log.warn("登录失败: 用户不存在, username={}", request.getUsername());
                log.debug("DEBUG: 返回失败响应 - 用户不存在");
                return LoginResponse.fail("用户名或密码错误");
            }

            log.debug("DEBUG: 用户查询成功, userId={}, 开始验证密码", user.getUserId());
            // 验证密码
            if (!PasswordUtil.verifyPassword(request.getPassword(), user.getPassword())) {
                log.warn("登录失败: 密码错误, username={}", request.getUsername());
                log.debug("DEBUG: 密码验证失败");
                return LoginResponse.fail("用户名或密码错误");
            }

            log.debug("DEBUG: 密码验证成功, 开始生成 JWT Token");
            // 生成 JWT Token
            String token = JwtUtil.generateToken(user.getUserId(), user.getUserName());
            log.debug("DEBUG: JWT Token 生成成功, token={}", token.substring(0, 20) + "...");

            log.info("用户登录成功: userId={}, userName={}", user.getUserId(), user.getUserName());
            log.info("========== 用户登录请求结束 ==========");

            // 返回成功响应
            return LoginResponse.success(token, user.getUserId(), user.getUserName(), user.getName());

        } catch (Exception e) {
            log.error("登录过程中发生错误: username={}, error={}", request.getUsername(), e.getMessage(), e);
            log.debug("DEBUG: 异常堆栈", e);
            return LoginResponse.fail("系统错误，请稍后重试");
        }
    }

    /**
     * 创建用户（注册）
     */
    @Transactional
    public boolean register(String username, String password, String name, String email, String phone) {
        log.info("用户注册请求: username={}, name={}, email={}", username, name, email);

        try {
            // 通过 Repository 检查用户是否已存在
            if (userRepository.existsByUsername(username)) {
                log.warn("注册失败: 用户名已存在, username={}", username);
                return false;
            }

            // 创建用户对象
            SysUser user = new SysUser();
            user.setUserId(UUID.randomUUID().toString().replace("-", ""));
            user.setUserName(username);
            user.setPassword(PasswordUtil.hashPassword(password));
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setStatus("在职");
            user.setIsDelete(0);
            user.setIsActivated(1);
            user.setIsCloud(0);

            // 通过 Repository 保存到数据库
            boolean success = userRepository.save(user);

            if (success) {
                log.info("用户注册成功: userId={}, userName={}", user.getUserId(), user.getUserName());
            } else {
                log.warn("用户注册失败: username={}", username);
            }

            return success;

        } catch (Exception e) {
            log.error("注册过程中发生错误: username={}, error={}", username, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 验证 Token
     */
    public boolean validateToken(String token) {
        log.debug("验证Token请求");
        try {
            // 首先检查Token是否在黑名单中
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token验证失败: Token已在黑名单中（用户已登出）");
                return false;
            }

            boolean isValid = JwtUtil.validateToken(token);
            if (isValid) {
                String username = JwtUtil.getUsername(token);
                log.debug("Token验证成功: username={}", username);
            } else {
                log.warn("Token验证失败: Token无效或已过期");
            }
            return isValid;
        } catch (Exception e) {
            log.error("Token验证过程中发生错误: error={}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 从 Token 获取用户名
     */
    public String getUsernameFromToken(String token) {
        log.debug("从Token获取用户名");
        try {
            String username = JwtUtil.getUsername(token);
            log.debug("成功从Token获取用户名: username={}", username);
            return username;
        } catch (Exception e) {
            log.error("从Token获取用户名失败: error={}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 用户登出
     * 将Token加入黑名单，使其失效
     */
    public boolean logout(String token) {
        log.info("用户登出请求");
        try {
            // 验证Token是否有效（不检查黑名单）
            if (!JwtUtil.validateToken(token)) {
                log.warn("登出失败: Token无效或已过期");
                return false;
            }

            // 获取Token过期时间
            java.util.Date expirationDate = JwtUtil.getExpirationDate(token);
            if (expirationDate == null) {
                log.warn("登出失败: 无法获取Token过期时间");
                return false;
            }

            // 获取用户信息
            String username = JwtUtil.getUsername(token);

            // 将Token加入黑名单
            tokenBlacklistService.addToBlacklist(token, expirationDate);

            log.info("用户登出成功: username={}", username);
            return true;

        } catch (Exception e) {
            log.error("登出过程中发生错误: error={}", e.getMessage(), e);
            return false;
        }
    }
}
