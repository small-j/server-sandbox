package server.sandbox.pinterestclone.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import server.sandbox.pinterestclone.jwt.dto.JwtTokenHeaderForm;
import server.sandbox.pinterestclone.jwt.dto.UserInfo;

import java.util.Date;

public class JwtProvider {

    public String createJwtToken(UserInfo userInfoDto) {
        return JWT.create()
                .withSubject(userInfoDto.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
//                .withClaim("id", userInfoDto.getId())
                .withClaim("email", userInfoDto.getEmail())
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

    public String getEmailFromJwtToken(String token) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token)
                .getClaim("email").asString();
    }
}

