package com.serversandbox.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.serversandbox.auth.auth.PrincipalDetails;
import com.serversandbox.auth.filter.JwtProperties;
import com.serversandbox.auth.model.dto.JwtTokenHeaderForm;

import java.util.Date;

public class JwtProvider {

    public String createJwtToken(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public JwtTokenHeaderForm getJwtTokenHeaderForm(String jwtToken) {
        return new JwtTokenHeaderForm(JwtProperties.TOKEN_HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    }
}
