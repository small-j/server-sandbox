package com.serversandbox.auth.config;

import com.serversandbox.auth.filter.JwtAuthenticationFilter;
import com.serversandbox.auth.filter.JwtAuthorizationFilter;
import com.serversandbox.auth.jwt.JwtProvider;
import com.serversandbox.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final UserRepository userRepository;
    private final CorsConfig corsConfig;
    private final JwtProvider jwtProvider;
// security config에서는 authenticationManager를 의존성주입으로 받을 수 없다.

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .apply(new MyCustomDsl());
        http
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/join", "/login").permitAll()
                .anyRequest().authenticated());

        return http.build();
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            // authentication manager를 이 configure 메서드 내부에서 getSharedObject를 통해 가져올 떄는 객체가 존재한다.
            // 하지만 이 authentication manager 객체가 setSharedObject 메서드를 통해 설정되는 시점이전에는 없기 때문에 그 이후에 사용할 수 있다.
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProvider))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository, jwtProvider));
        }
    }
}
