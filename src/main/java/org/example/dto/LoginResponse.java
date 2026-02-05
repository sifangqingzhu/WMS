package org.example.dto;

/**
 * 登录响应 DTO
 */
public class LoginResponse {
    /**
     * 是否成功
     */
    private boolean success;

    /**
     * JWT Token
     */
    private String token;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 消息
     */
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String token, Long userId, String username, String realName, String message) {
        this.success = success;
        this.token = token;
        this.userId = userId;
        this.username = username;
        this.realName = realName;
        this.message = message;
    }

    /**
     * 创建失败的响应
     */
    public static LoginResponse fail(String message) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    /**
     * 创建成功的响应
     */
    public static LoginResponse success(String token, Long userId, String username, String realName) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(true);
        response.setToken(token);
        response.setUserId(userId);
        response.setUsername(username);
        response.setRealName(realName);
        response.setMessage("登录成功");
        return response;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
