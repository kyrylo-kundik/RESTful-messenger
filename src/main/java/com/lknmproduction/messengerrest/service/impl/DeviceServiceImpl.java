package com.lknmproduction.messengerrest.service.impl;

import com.lknmproduction.messengerrest.domain.Device;
import com.lknmproduction.messengerrest.repositories.DeviceRepository;
import com.lknmproduction.messengerrest.service.DeviceService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
        return deviceRepository.findById(id).orElse(null);
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

    @Override
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public void deleteDevice(String deviceId) {
        deviceRepository.deleteById(deviceId);
    }
}
