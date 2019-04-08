package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import com.lknmproduction.messengerrest.domain.utils.PhoneDeviceBaseLogin;
import com.lknmproduction.messengerrest.domain.utils.StringResponsePinCode;
import com.lknmproduction.messengerrest.domain.utils.StringResponseToken;
import com.lknmproduction.messengerrest.repositories.redis.RedisRepository;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;
import static com.lknmproduction.messengerrest.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

    public static final String BASE_URL = "/api/v1/users";

    private final UserService userService;
    private final RedisRepository redisRepository;
    private final JwtTokenService jwtTokenService;
    private final TwilioService twilioService;
    private final HttpServletRequest req;
    private static final Random RANDOM = new Random(System.nanoTime());

    public LoginController(UserService userService, RedisRepository redisRepository, JwtTokenService jwtTokenService, TwilioService twilioService, HttpServletRequest req) {
        this.userService = userService;
        this.redisRepository = redisRepository;
        this.jwtTokenService = jwtTokenService;
        this.twilioService = twilioService;
        this.req = req;
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody PhoneDeviceBaseLogin baseLogin) {

        String token = req.getHeader(HEADER_STRING);

        if (token != null && token.startsWith(TOKEN_PREFIX)) {
            // checking if user was already active
            DecodedJWT jwt = jwtTokenService.decodeToken(token);
            if (jwt.getClaim("isActive").asBoolean())
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        String pinCode = generatePinCode();
//        StringResponsePinCode responsePinCode = new StringResponsePinCode();
//        responsePinCode.setPinCode(pinCode);

//        StringResponsePinCodeJWT responsePinCode = new StringResponsePinCodeJWT();
//        responsePinCode.setPinCode(pinCode);

        DeviceConfirmRedis device = new DeviceConfirmRedis();
        device.setDeviceId(baseLogin.getDeviceId());
        device.setPinCode(pinCode);
        redisRepository.save(device);

        token = jwtTokenService.encodeToken(baseLogin.getPhoneNumber(), baseLogin.getDeviceId(), false, false);
        twilioService.sendMessage(baseLogin.getPhoneNumber(), "Your pin code: " + pinCode);

        StringResponseToken responseToken = new StringResponseToken();
        responseToken.setToken(token);
//        responsePinCode.setToken(token);
//        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        return new ResponseEntity<>(responseToken, HttpStatus.OK);
    }

    @PostMapping("/confirmLogin")
    @ResponseBody
    public ResponseEntity<?> confirmLogin(@RequestHeader(HEADER_STRING) String token, @RequestBody StringResponsePinCode pinCode) {
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
        if (!pinCode.getPinCode().equals(device.getPinCode()))
            return new ResponseEntity<>("Pin code is not correct!", HttpStatus.UNAUTHORIZED);

        redisRepository.deleteById(deviceId);

        List<Device> deviceList = userService.userDevicesByPhoneNumber(phoneNumber);

        if (deviceList != null && deviceList.stream().anyMatch(d -> d.getId().equals(deviceId))) {
            token = jwtTokenService.encodeToken(phoneNumber, deviceId, true, true);
        } else {
            token = jwtTokenService.encodeToken(phoneNumber, deviceId, true, false);
        }
        StringResponseToken responseToken = new StringResponseToken();
        responseToken.setToken(token);
//        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        return new ResponseEntity<>(responseToken, HttpStatus.OK);
    }

    @PostMapping("/resendCode")
    @ResponseBody
    public ResponseEntity<?> resendCode(@RequestHeader(HEADER_STRING) String token) {

        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        DecodedJWT jwt = jwtTokenService.decodeToken(token);

        //TODO rethink this
//        if (jwt.getClaim("isActive").asBoolean())
//            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

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

        twilioService.sendMessage(jwt.getClaim("phoneNumber").asString(), "Your pin code: " + pinCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generatePinCode() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }

}
