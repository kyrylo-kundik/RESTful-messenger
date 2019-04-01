package com.lknmproduction.messengerrest.security;

public class SecurityConstants {
    public static final String SECRET = "SuperSecretLKNMMEssengerFor_J4aBBa_33";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/v1/users/signUp";
    public static final String CONFIRM_LOGIN_URL = "/api/v1/users/confirmLogin";
}
