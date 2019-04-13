package com.lknmproduction.messengerrest.controllers.utils;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import com.lknmproduction.messengerrest.domain.utils.requests.PhoneDeviceBaseLogin;
import com.lknmproduction.messengerrest.domain.utils.responses.StringResponsePinCode;
import com.lknmproduction.messengerrest.domain.utils.responses.StringResponseToken;
import com.lknmproduction.messengerrest.service.DeviceService;
import com.lknmproduction.messengerrest.service.redis.ConfirmPinCodeRedisService;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Random;

import static com.lknmproduction.messengerrest.security.SecurityConstants.*;
import static com.lknmproduction.messengerrest.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping(LoginController.BASE_URL)
public class LoginController {

    public static final String BASE_URL = "/api/v1/users";

    private final UserService userService;
    private final DeviceService deviceService;
    private final ConfirmPinCodeRedisService redisService;
    private final JwtTokenService jwtTokenService;
    private final TwilioService twilioService;
    private final HttpServletRequest req;
    private static final Random RANDOM = new Random(System.nanoTime());

    public LoginController(UserService userService, DeviceService deviceService, ConfirmPinCodeRedisService redisService, JwtTokenService jwtTokenService, TwilioService twilioService, HttpServletRequest req) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.redisService = redisService;
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
        String deviceId = baseLogin.getDeviceId();
//        StringResponsePinCode responsePinCode = new StringResponsePinCode();
//        responsePinCode.setPinCode(pinCode);

//        StringResponsePinCodeJWT responsePinCode = new StringResponsePinCodeJWT();
//        responsePinCode.setPinCode(pinCode);

        if (redisService.findById(deviceId) != null)
            redisService.deleteById(deviceId);

        DeviceConfirmRedis device = new DeviceConfirmRedis();

        device.setPinCode(pinCode);
        device.setDeviceId(deviceId);

        redisService.printTest("Hello! Please pabotai");
        redisService.addDevice(device);


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

        DeviceConfirmRedis device = redisService.findById(deviceId);

        if (device == null)
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        if (!pinCode.getPinCode().equals(device.getPinCode()))
            return new ResponseEntity<>("Pin code is not correct!", HttpStatus.UNAUTHORIZED);

        redisService.deleteById(deviceId);

        User user = userService.findUserByPhoneNumber(phoneNumber);

        if (user != null) {

            List<Device> deviceList = user.getDeviceList();

            boolean wasSet = deviceList.stream().anyMatch(device1 -> device1.getId().equals(deviceId));

            if (!wasSet) {
                Device userDevice = new Device();
                userDevice.setId(deviceId);
                userDevice.setIsActive(true);
                List<Device> devices = user.getDeviceList();
                devices.add(userDevice);
                user.setDeviceList(devices);
                userService.saveUser(user);
            } else {
                deviceService.setActiveness(deviceId, true);
            }

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

//        if (jwt.getClaim("isActive").asBoolean())
//            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);

        String deviceId = jwt.getClaim("deviceId").asString();
        DeviceConfirmRedis deviceConfirmRedis = redisService.findById(deviceId);
        String pinCode;
        if (deviceConfirmRedis == null) {

            pinCode = generatePinCode();

            DeviceConfirmRedis newDevice = new DeviceConfirmRedis();
            newDevice.setPinCode(pinCode);
            newDevice.setDeviceId(deviceId);

            redisService.addDevice(newDevice);

        } else {
            pinCode = deviceConfirmRedis.getPinCode();
        }

        twilioService.sendMessage(jwt.getClaim("phoneNumber").asString(), "Your pin code: " + pinCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generatePinCode() {
        return String.format("%06d", RANDOM.nextInt(1000000));
    }

}
