package org.example.dto;

/**
 * Token 验证请求
 */
public class TokenValidateRequest {
    private String token;

    public TokenValidateRequest() {
    }

    public TokenValidateRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
