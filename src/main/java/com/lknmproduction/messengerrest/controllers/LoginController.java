package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.utils.StringResponseJWT;
import com.lknmproduction.messengerrest.domain.utils.StringResponsePinCode;
import com.lknmproduction.messengerrest.domain.utils.StringResponseResetPinCode;
import com.lknmproduction.messengerrest.service.DeviceService;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Random;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;
import static com.lknmproduction.messengerrest.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

    public static final String BASE_URL = "/api/v1/user";

    private final DeviceService deviceService;
    private final JwtTokenService jwtTokenService;
    private static final Random RANDOM = new Random(System.nanoTime());

    public LoginController(DeviceService deviceService, JwtTokenService jwtTokenService) {
        this.deviceService = deviceService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestHeader(HEADER_STRING) String token, @RequestParam String phoneNumber, @RequestParam String deviceId, HttpServletResponse res) {

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            // checking if user was already active
            DecodedJWT jwt = jwtTokenService.decodeToken(token);
            if (jwt.getClaim("isActive").asBoolean())
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        String pinCode = String.format("%04d", RANDOM.nextInt(10000));
        StringResponsePinCode responsePinCode = new StringResponsePinCode();
        responsePinCode.setPinCode(pinCode);
        //TODO add number, device and pin code to the redis store

        token = jwtTokenService.encodeToken(phoneNumber, deviceId, false, false);
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        return new ResponseEntity<>(responsePinCode, HttpStatus.OK);
    }

    @PostMapping("/confirmLogin")
    @ResponseBody
    public ResponseEntity<?> confirmLogin(@RequestHeader(HEADER_STRING) String token, @RequestParam String pinCode, HttpServletResponse res) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DecodedJWT jwt = jwtTokenService.decodeToken(token);

        String deviceId = jwt.getClaim("deviceId").asString();
        String phoneNumber = jwt.getClaim("phoneNumber").asString();
        // TODO check pin in redis store

        if (deviceService.getDeviceById(deviceId) != null) {
            token = jwtTokenService.encodeToken(phoneNumber, deviceId, true, true);
        } else {
            token = jwtTokenService.encodeToken(phoneNumber, deviceId, true, false);
        }
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/resendCode")
    @ResponseBody
    public ResponseEntity<?> resendCode(@RequestHeader(HEADER_STRING) String token) {

        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DecodedJWT jwt = jwtTokenService.decodeToken(token);

        if (jwt.getClaim("isActive").asBoolean())
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        String deviceId = jwt.getClaim("deviceId").asString();
        // TODO get pin from redis store

        StringResponseResetPinCode resetPinCode = new StringResponseResetPinCode();
        resetPinCode.setPinCode("1111");
        return new ResponseEntity<>(resetPinCode, HttpStatus.OK);
    }

}
