package com.serversandbox.auth.filter;

public interface JwtProperties {
    String SECRET = "secret";
    int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10; // 10일.
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_HEADER_STRING = "Authorization";
}
