package server.sandbox.pinterestclone.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import server.sandbox.pinterestclone.jwt.JwtProvider;

@Configuration
public class JwtConfig {

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }
}
