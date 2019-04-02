package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.domain.utils.StringResponseJWT;
import com.lknmproduction.messengerrest.domain.utils.StringResponsePinCode;
import com.lknmproduction.messengerrest.domain.utils.StringResponseResetPinCode;
import com.lknmproduction.messengerrest.service.DeviceService;
import com.lknmproduction.messengerrest.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;
import static com.lknmproduction.messengerrest.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

    public static final String BASE_URL = "/api/v1/user";

    private final UserService userService;
    private final DeviceService deviceService;
    private static final Random RANDOM = new Random(System.nanoTime());

    public LoginController(UserService userService, DeviceService deviceService) {
        this.userService = userService;
        this.deviceService = deviceService;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestHeader(HEADER_STRING) String token, @RequestParam String phoneNumber, @RequestParam String deviceId) {

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            // checking if user was already active
            DecodedJWT jwt = decodeJWT(token);
            if (jwt.getClaim("isActive").asBoolean())
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        String pinCode = String.format("%04d", RANDOM.nextInt(10000));
        StringResponsePinCode responsePinCode = new StringResponsePinCode();
        responsePinCode.setPinCode(pinCode);
        //TODO add number, device and pin code to the redis store

        token = generateJWT(phoneNumber, deviceId, false, false);
        responsePinCode.setJwtToken(token);

        return new ResponseEntity<>(responsePinCode, HttpStatus.OK);
    }

    @PostMapping("/confirmLogin")
    @ResponseBody
    public ResponseEntity<?> confirmLogin(@RequestHeader(HEADER_STRING) String token, @RequestParam String pinCode) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DecodedJWT jwt = decodeJWT(token);

        String deviceId = jwt.getClaim("deviceId").asString();
        String phoneNumber = jwt.getClaim("phoneNumber").asString();
        // TODO check pin in redis store

        if (deviceService.getDeviceById(deviceId) != null) {
            token = generateJWT(phoneNumber, deviceId, true, true);
        } else {
            token = generateJWT(phoneNumber, deviceId, true, false);

            Device device = new Device();
            device.setId(deviceId);
            device.setIsActive(true);

            List<Device> devices = new ArrayList<>();
            devices.add(device);

            User user = new User();
            user.setPhoneNumber(phoneNumber);
            user.setDeviceList(devices);

            userService.saveUser(user);
        }
        StringResponseJWT responseJWT = new StringResponseJWT();
        responseJWT.setNewJWT(token);
        return new ResponseEntity<>(responseJWT, HttpStatus.OK);
    }

    @PostMapping("/resendCode")
    @ResponseBody
    public ResponseEntity<?> resendCode(@RequestHeader(HEADER_STRING) String token) {

        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DecodedJWT jwt = decodeJWT(token);

        if (jwt.getClaim("isActive").asBoolean())
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        String deviceId = jwt.getClaim("deviceId").asString();
        // TODO get pin from redis store

        StringResponseResetPinCode resetPinCode = new StringResponseResetPinCode();
        resetPinCode.setPinCode("1111");
        return new ResponseEntity<>(resetPinCode, HttpStatus.OK);
    }

    private String generateJWT(String phoneNumber, String deviceId, boolean isActive, boolean isSignedup) {
        return JWT.create()
                .withClaim("phoneNumber", phoneNumber)
                .withClaim("isActive", isActive)
                .withClaim("isSignedup", isSignedup)
                .withClaim("deviceId", deviceId)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    private DecodedJWT decodeJWT(String token) {
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""));
    }

}
