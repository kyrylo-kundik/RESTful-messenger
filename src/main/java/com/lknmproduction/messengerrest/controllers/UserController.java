package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.domain.utils.StringResponseToken;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lknmproduction.messengerrest.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/user";
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final TwilioService twilioService;

    public UserController(UserService userService, JwtTokenService jwtTokenService, TwilioService twilioService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.twilioService = twilioService;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteUserById(@PathVariable Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.findUsers();
    }

    @PostMapping("/createUser")
    @ResponseBody
    public ResponseEntity<?> createUser(@RequestHeader(HEADER_STRING) String token, @RequestBody User user) {
        DecodedJWT decodedJWT = jwtTokenService.decodeToken(token);

        if (decodedJWT.getClaim("isActive").asBoolean() && decodedJWT.getClaim("isSignedup").asBoolean()) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }

        String phoneNumber = decodedJWT.getClaim("phoneNumber").asString();
        String deviceId = decodedJWT.getClaim("deviceId").asString();

        user.setPhoneNumber(phoneNumber);

        Device device = new Device();
        device.setIsActive(true);
        device.setId(deviceId);

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);

        user.setDeviceList(deviceList);
        userService.saveUser(user);

        StringResponseToken responseToken = new StringResponseToken();
        responseToken.setToken(jwtTokenService.encodeToken(phoneNumber, deviceId, true, true));

        return new ResponseEntity<>(responseToken, HttpStatus.CREATED);
    }

    @GetMapping("/chatToken")
    public String getChatToken(@RequestHeader(HEADER_STRING) String jwtTokenUser) {
        return twilioService.getChatToken(jwtTokenUser);
    }

}
