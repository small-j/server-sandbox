package server.sandbox.pinterestclone.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import server.sandbox.pinterestclone.auth.CustomUserDetailsService;
import server.sandbox.pinterestclone.filter.JwtAuthFilter;
import server.sandbox.pinterestclone.jwt.JwtProvider;
import server.sandbox.pinterestclone.service.enums.UserRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final CorsConfig corsConfig;
    private final JwtProvider jwtProvider;

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
                .addFilter(corsConfig.corsFilter())
                .addFilterBefore(new JwtAuthFilter(jwtProvider, customUserDetailsService), UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/join", "/user/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/category").hasRole(UserRole.ADMIN.getNonPrefixRole())
//                        .requestMatchers(HttpMethod.DELETE, "/category").hasRole(UserRole.ADMIN.getNonPrefixRole()) // TODO : 추후 delete 기능 추가 시 활성화.
                        .anyRequest().authenticated());

        return http.build();
    }
}
