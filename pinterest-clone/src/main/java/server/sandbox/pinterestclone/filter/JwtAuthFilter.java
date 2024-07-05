package server.sandbox.pinterestclone.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import server.sandbox.pinterestclone.auth.CustomUserDetails;
import server.sandbox.pinterestclone.auth.CustomUserDetailsService;
import server.sandbox.pinterestclone.jwt.JwtProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (jwtProvider.checkRequestTokenHeader(request)) {
            String token = jwtProvider.getTokenToHttpHeader(request);

            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromJwtToken(token);

                CustomUserDetails customUserDetails = customUserDetailsService.loadUserByUsername(email);
                Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
