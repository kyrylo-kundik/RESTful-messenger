package com.lknmproduction.messengerrest.domain.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("Device")
public class DeviceConfirmRedis implements Serializable {

    @Id
    private String deviceId;
    private String pinCode;

}
