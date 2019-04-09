package com.lknmproduction.messengerrest.service;

import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;

public interface ConfirmPinCodeRedisService {

    DeviceConfirmRedis addDevice(DeviceConfirmRedis deviceConfirmRedis);

    DeviceConfirmRedis findById(String id);

    DeviceConfirmRedis deleteById(String id);

}
