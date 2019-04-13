package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.Device;

import java.util.List;

public interface DeviceService {

    Device getDeviceById(String id);

    List<String> getAllDevicePushId();

    void setPushId(String deviceId, String pushId);

    void setActiveness(String deviceId, boolean isActive);

}
