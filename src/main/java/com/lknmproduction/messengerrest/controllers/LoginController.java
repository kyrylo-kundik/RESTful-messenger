package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import com.lknmproduction.messengerrest.domain.utils.StringResponsePinCode;
import com.lknmproduction.messengerrest.repositories.redis.RedisRepository;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.Random;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;
import static com.lknmproduction.messengerrest.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

    public static final String BASE_URL = "/api/v1/user";

    private final UserService userService;
    private final RedisRepository redisRepository;
    private final JwtTokenService jwtTokenService;
    private static final Random RANDOM = new Random(System.nanoTime());

    public LoginController(UserService userService, RedisRepository redisRepository, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.redisRepository = redisRepository;
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

        String pinCode = generatePinCode();
        StringResponsePinCode responsePinCode = new StringResponsePinCode();
        responsePinCode.setPinCode(pinCode);

        DeviceConfirmRedis device = new DeviceConfirmRedis();
        device.setDeviceId(deviceId);
        device.setPinCode(pinCode);
        redisRepository.save(device);

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

        Optional optional = redisRepository.findById(deviceId);

        if (!optional.isPresent())
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        DeviceConfirmRedis device = (DeviceConfirmRedis) optional.get();
        if (!pinCode.equals(device.getPinCode()))
            return new ResponseEntity<>("Pin code is not correct!", HttpStatus.UNAUTHORIZED);

        redisRepository.deleteById(deviceId);

        if (userService.userDevicesByPhoneNumber(phoneNumber).stream().anyMatch(d -> d.getId().equals(deviceId))) {
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
        Optional optional = redisRepository.findById(deviceId);
        String pinCode;
        if (!optional.isPresent()) {
            pinCode = generatePinCode();

            DeviceConfirmRedis newDevice = new DeviceConfirmRedis();
            newDevice.setPinCode(pinCode);
            newDevice.setDeviceId(deviceId);

            redisRepository.save(newDevice);
        } else {
            pinCode = ((DeviceConfirmRedis) optional.get()).getPinCode();
        }

        StringResponsePinCode resetPinCode = new StringResponsePinCode();

        resetPinCode.setPinCode(pinCode);
        return new ResponseEntity<>(resetPinCode, HttpStatus.OK);
    }

    private String generatePinCode() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }

}
