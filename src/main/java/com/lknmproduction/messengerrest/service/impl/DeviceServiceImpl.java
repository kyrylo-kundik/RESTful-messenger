package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.repositories.DeviceRepository;
import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@CacheConfig(cacheNames = {"devices"})
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Cacheable
    public Device getDeviceById(String id) {
        Optional optional = deviceRepository.findById(id);
        if (optional.isPresent())
            return (Device) optional.get();
        return null;
    }

    @Override
    @Cacheable
    public List<String> getAllDevicePushId() {
        return deviceRepository.findAllPushIds();
    }

    @Override
    public void setPushId(String deviceId, String pushId) {
        deviceRepository.setPushIdToDevice(deviceId, pushId);
    }

    @Override
    public void setActiveness(String deviceId, boolean isActive) {
        deviceRepository.setDeviceIsActive(deviceId, isActive);
    }
}
