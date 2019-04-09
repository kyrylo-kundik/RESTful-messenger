package com.lknmproduction.messengerrest.controllers;

import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
