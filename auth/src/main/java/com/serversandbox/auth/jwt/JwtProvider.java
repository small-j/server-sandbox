package com.serversandbox.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.serversandbox.auth.model.dto.JwtTokenHeaderForm;
import com.serversandbox.auth.model.dto.UserInfoDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Date;

public class JwtProvider {

    public String createJwtToken(UserInfoDto userInfoDto) {
        return JWT.create()
                .withSubject(userInfoDto.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", userInfoDto.getId())
                .withClaim("username", userInfoDto.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public JwtTokenHeaderForm getJwtTokenHeaderForm(String jwtToken) {
        return new JwtTokenHeaderForm(JwtProperties.TOKEN_HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }

    public boolean checkRequestTokenHeader(HttpServletRequest request) {
        String header = request.getHeader(JwtProperties.TOKEN_HEADER_STRING);
        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX))
            return false;
        return true;
    }

    public String getTokenToHttpHeader(HttpServletRequest request) {
        String header = request.getHeader(JwtProperties.TOKEN_HEADER_STRING);
        return header.replace(JwtProperties.TOKEN_PREFIX, "");
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                    .getClaim("username").asString();
    }
}
