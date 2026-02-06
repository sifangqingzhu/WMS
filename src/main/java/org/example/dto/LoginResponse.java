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
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 消息
     */
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String token, String userId, String userName, String name, String message) {
        this.success = success;
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.name = name;
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
    public static LoginResponse success(String token, String userId, String userName, String name) {
        LoginResponse response = new LoginResponse();
        response.setSuccess(true);
        response.setToken(token);
        response.setUserId(userId);
        response.setUserName(userName);
        response.setName(name);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
