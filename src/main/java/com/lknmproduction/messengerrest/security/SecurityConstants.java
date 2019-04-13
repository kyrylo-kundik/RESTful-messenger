package com.lknmproduction.messengerrest.security;

public class SecurityConstants {
    public static final String SECRET = "SuperSecretLKNMMEssengerFor_J4aBBa_33";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "JWT ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_URL = "/api/v1/users/login";
    public static final String CONFIRM_LOGIN_URL = "/api/v1/users/confirmLogin";
    public static final String RESEND_CODE_URL = "/api/v1/users/resendCode";
    public static final String CREATE_USER_URL = "/api/v1/user/createUser";
}
