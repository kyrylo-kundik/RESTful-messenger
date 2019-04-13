package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.domain.utils.requests.EditingUser;
import com.lknmproduction.messengerrest.domain.utils.requests.PhoneList;
import com.lknmproduction.messengerrest.domain.utils.responses.StringResponseChatToken;
import com.lknmproduction.messengerrest.domain.utils.responses.StringResponseToken;
import com.lknmproduction.messengerrest.service.DeviceService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.lknmproduction.messengerrest.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {

    public static final String BASE_URL = "/api/v1/user";
    private final UserService userService;
    private final DeviceService deviceService;
    private final JwtTokenService jwtTokenService;
    private final TwilioService twilioService;

    public UserController(UserService userService, DeviceService deviceService, JwtTokenService jwtTokenService, TwilioService twilioService) {
        this.userService = userService;
        this.deviceService = deviceService;
        this.jwtTokenService = jwtTokenService;
        this.twilioService = twilioService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<?> getUser(@RequestHeader(HEADER_STRING) String token) {
        User user = userService.findUserByPhoneNumber(jwtTokenService.decodeToken(token).getClaim("phoneNumber").asString());
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    @ResponseBody
    public User editUser(@RequestHeader(HEADER_STRING) String token, @RequestBody EditingUser user) {

        DecodedJWT jwt = jwtTokenService.decodeToken(token);
        String phoneNumber = jwt.getClaim("phoneNumber").asString();

        User existedUser = userService.findUserByPhoneNumber(phoneNumber);
        if (user.getBio() != null && !user.getBio().equals(""))
            existedUser.setBio(user.getBio());
        if (user.getLastSeen() != null && !user.getLastSeen().equals(new Date(0)))
            existedUser.setLastSeen(user.getLastSeen());
        if (user.getPhotoUrl() != null && !user.getPhotoUrl().equals(""))
            existedUser.setPhotoUrl(user.getPhotoUrl());
        if (user.getUsername() != null && !user.getUsername().equals(""))
            existedUser.setUsername(user.getUsername());
        if (user.getLastName() != null && !user.getLastName().equals(""))
            existedUser.setLastName(user.getLastName());
        if (user.getFirstName() != null && !user.getFirstName().equals(""))
            existedUser.setFirstName(user.getFirstName());

        userService.saveUser(existedUser);
        return existedUser;
    }

    @PostMapping("/getUsersByPhones")
    @ResponseBody
    public Set<User> getUsersByPhones(@RequestBody PhoneList phoneList) {
        return phoneList
                .getPhoneList()
                .stream()
                .map(userService::findUserByPhoneNumberLike)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
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
    @ResponseBody
    public StringResponseChatToken getChatToken(@RequestHeader(HEADER_STRING) String jwtTokenUser) {
        StringResponseChatToken token = new StringResponseChatToken();
        token.setChatToken(twilioService.getChatToken(jwtTokenService.decodeToken(jwtTokenUser).getClaim("phoneNumber").asString()));
        return token;
    }

    @GetMapping("/logout")
    @ResponseBody
    public StringResponseToken userLogout(@RequestHeader(HEADER_STRING) String token) {
        DecodedJWT decodedJWT = jwtTokenService.decodeToken(token);

        String deviceId = decodedJWT.getClaim("deviceId").asString();

        deviceService.setActiveness(deviceId, false);

        StringResponseToken responseToken = new StringResponseToken();
        responseToken.setToken(jwtTokenService.encodeToken(decodedJWT.getClaim("phoneNumber").asString(), deviceId, false, true));
        return responseToken;
    }

}
