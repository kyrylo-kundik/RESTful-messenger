package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.domain.User;
import com.lknmproduction.messengerrest.service.TwilioCredentialService;
import com.lknmproduction.messengerrest.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping(SignUpSignInController.BASE_URL)
public class SignUpSignInController {

    public static final String BASE_URL = "/api/v1/user";

    private final UserService userService;
    private final TwilioCredentialService twilioCredentialService;

    public SignUpSignInController(UserService userService, TwilioCredentialService twilioCredentialService) {
        this.userService = userService;
        this.twilioCredentialService = twilioCredentialService;
    }

    @PostMapping("/signUp")
    public String signUp(@RequestBody User user, @RequestParam String deviceId) {
        List<Device> deviceList = new LinkedList<>();
        Device device = new Device();
        device.setId(deviceId);
        deviceList.add(device);
        user.setDeviceList(deviceList);
        userService.saveUser(user);
        return "Hello";
    }

    @PostMapping("/confirmLogin")
    public String confirmLogin() {
        return "confirmed";
    }


}
