package com.serversandbox.auth.jwt;

public interface JwtProperties {
    String SECRET = "secret";
    int EXPIRATION_TIME = 1000 * 60 * 3; // 3분.
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_HEADER_STRING = "Authorization";
}
