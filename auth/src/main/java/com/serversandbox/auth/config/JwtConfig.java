package com.serversandbox.auth.config;

import com.serversandbox.auth.jwt.JwtProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }
}
