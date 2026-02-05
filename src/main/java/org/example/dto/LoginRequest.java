package org.example.dto;

/**
 * 登录请求 DTO
 */
public class LoginRequest {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
