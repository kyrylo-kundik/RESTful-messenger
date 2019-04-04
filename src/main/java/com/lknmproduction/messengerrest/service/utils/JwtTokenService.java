package com.lknmproduction.messengerrest.service.utils;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface JwtTokenService {

    DecodedJWT decodeToken(String token);

    String encodeToken(String phoneNumber, String deviceId, boolean isActive, boolean isSignedup);

}
