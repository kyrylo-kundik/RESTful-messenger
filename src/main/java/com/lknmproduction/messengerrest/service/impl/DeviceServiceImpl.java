package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.repositories.DeviceRepository;
import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device getDeviceById(String id) {
        return deviceRepository.getOne(id);
    }
}
