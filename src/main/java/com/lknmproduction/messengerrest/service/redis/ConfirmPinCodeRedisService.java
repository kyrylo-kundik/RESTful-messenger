package com.lknmproduction.messengerrest.service.redis;

import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;

public interface ConfirmPinCodeRedisService {

    DeviceConfirmRedis addDevice(DeviceConfirmRedis deviceConfirmRedis);

    DeviceConfirmRedis findById(String id);

    DeviceConfirmRedis deleteById(String id);

    void printTest(String print);
}
