package com.serversandbox.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serversandbox.auth.auth.PrincipalDetails;
import com.serversandbox.auth.jwt.JwtProvider;
import com.serversandbox.auth.model.LoginRequestDto;
import com.serversandbox.auth.model.dto.JwtTokenHeaderForm;
import com.serversandbox.auth.model.dto.UserInfoDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// http://localhost:8080/login 요청시에 이 필터가 실행된다.(post)
// 이 필터를 다시 securityConfig에 등록하면 동작한다.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    // 1. username, password 받아서
    // 2. 정상인지 로그인 시도를 해본다.
    // 3. 로그인이 되면 PrincipalDetailsService -> loadUserByUsername가 호출된다.

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword()
                );

        // 로그인 시도. 만약 로그인이 정상적으로 됬다면 그대로 반환된다.
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        UserInfoDto userInfoDto = UserInfoDto.of(principalDetails.getUser());
        String jwtToken = jwtProvider.createJwtToken(userInfoDto);
        JwtTokenHeaderForm jwtTokenHeaderForm = jwtProvider.getJwtTokenHeaderForm(jwtToken);
        response.addHeader(jwtTokenHeaderForm.getHeaderName(), jwtTokenHeaderForm.getJwtToken());
        // doFilter를 실행하지 않고 메서드를 끝내면 다음 필터가 실행되지 않고 유저에게 response가 반환된다.
    }
}
