package com.lknmproduction.messengerrest.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import com.lknmproduction.messengerrest.service.utils.twilio.TwilioCredentialService;
import com.lknmproduction.messengerrest.service.UserService;
import com.twilio.jwt.accesstoken.AccessToken;
import com.twilio.jwt.accesstoken.ChatGrant;
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
    private final TwilioCredentialService twilioCredentialService;

    public UserController(UserService userService, JwtTokenService jwtTokenService, TwilioCredentialService twilioCredentialService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
        this.twilioCredentialService = twilioCredentialService;
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

        if (!user.getPhoneNumber().equals(decodedJWT.getClaim("phoneNumber").asString()))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        Device device = new Device();
        device.setIsActive(true);
        device.setId(decodedJWT.getClaim("deviceId").asString());

        List<Device> deviceList = new ArrayList<>();
        deviceList.add(device);

        user.setDeviceList(deviceList);
        User createdUser = userService.saveUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/chatToken")
    public String getChatToken(@RequestHeader(HEADER_STRING) String jwtTokenUser) {

        ChatGrant grant = new ChatGrant();
        grant.setServiceSid(twilioCredentialService.getServiceSid());

        AccessToken token = new AccessToken.Builder(twilioCredentialService.getTwilioAccountSid(),
                twilioCredentialService.getTwilioApiKey(), twilioCredentialService.getTwilioApiSecret())
                .identity(jwtTokenUser).grant(grant).build();

        return token.toJwt();
    }

}
