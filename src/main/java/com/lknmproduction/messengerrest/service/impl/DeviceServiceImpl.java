package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.repositories.DeviceRepository;
import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device getDeviceById(String id) {
        Optional optional = deviceRepository.findById(id);
        if (optional.isPresent())
            return (Device) optional.get();
        return null;
    }
}
