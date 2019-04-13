package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.domain.utils.requests.DevicePushId;
import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lknmproduction.messengerrest.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping(DeviceController.BASE_URL)
public class DeviceController {

    public static final String BASE_URL = "/api/v1/devices";

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/getAllPushIds")
    public List<String> getAllPushIds() {
        return deviceService.getAllDevicePushId();
    }

    @PostMapping("/setPushId")
    public void setPishId(@RequestHeader(HEADER_STRING) String token, @RequestBody DevicePushId devicePushId) {

    }

}
