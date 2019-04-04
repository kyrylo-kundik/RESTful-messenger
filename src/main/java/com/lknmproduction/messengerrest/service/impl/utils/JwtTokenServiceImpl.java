package com.lknmproduction.messengerrest.service.impl.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    @Override
    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

    @Override
    public String encodeToken(String phoneNumber, String deviceId, boolean isActive, boolean isSignedup) {
        return JWT.create()
                .withClaim("phoneNumber", phoneNumber)
                .withClaim("isActive", isActive)
                .withClaim("isSignedup", isSignedup)
                .withClaim("deviceId", deviceId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }
}
