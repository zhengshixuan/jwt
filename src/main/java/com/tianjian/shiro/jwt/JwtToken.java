package com.tianjian.shiro.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * JwtToken:实现shiro的AuthenticationToken接口的类JwtToken
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
