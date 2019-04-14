package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.utils.requests.DevicePushId;
import com.lknmproduction.messengerrest.domain.utils.requests.PhoneList;
import com.lknmproduction.messengerrest.domain.utils.requests.SendNotifBody;
import com.lknmproduction.messengerrest.service.DeviceService;
import com.lknmproduction.messengerrest.service.UserService;
import com.lknmproduction.messengerrest.service.utils.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.lknmproduction.messengerrest.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping(DeviceController.BASE_URL)
public class DeviceController {

    public static final String BASE_URL = "/api/v1/devices";

    private final DeviceService deviceService;
    private final UserService userService;
    private final JwtTokenService tokenService;

    public DeviceController(DeviceService deviceService, UserService userService, JwtTokenService tokenService) {
        this.deviceService = deviceService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/getAllPushIds")
    public List<String> getAllPushIds() {
        return deviceService.getAllDevicePushId();
    }

    @PostMapping("/setPushId")
    @ResponseStatus(HttpStatus.OK)
    public void setPushId(@RequestHeader(HEADER_STRING) String token, @RequestBody DevicePushId devicePushId) {
        String deviceId = tokenService.decodeToken(token).getClaim("deviceId").asString();
        deviceService.setPushId(deviceId, devicePushId.getPushId());
    }

    @PostMapping("/sendNotifications")
    @ResponseBody
    public ResponseEntity<?> sendNotifications(@RequestBody SendNotifBody notifBody) {

        CompletableFuture<String> pushNotification = userService.sendNotifications(notifBody.getTitle(),
                notifBody.getBody(), notifBody.getPayload(), notifBody.getPhoneList());

        if (pushNotification == null)
            return new ResponseEntity<>("Push ids array length is 0", HttpStatus.BAD_REQUEST);

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);

    }

    @PostMapping("/getActivePushIds")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<String> getActivePushIds(@RequestBody PhoneList phoneList) {
        return userService.getPushIdsByPhoneNumbers(phoneList.getPhoneList());
    }

}
