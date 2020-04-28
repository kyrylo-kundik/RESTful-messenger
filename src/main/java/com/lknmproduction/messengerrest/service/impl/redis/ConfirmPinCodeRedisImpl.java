package com.lknmproduction.messengerrest.service.impl.redis;

import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import com.lknmproduction.messengerrest.repositories.redis.RedisRepository;
import com.lknmproduction.messengerrest.service.redis.ConfirmPinCodeRedisService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmPinCodeRedisImpl implements ConfirmPinCodeRedisService {

    private final RedisRepository redisRepository;

    public ConfirmPinCodeRedisImpl(RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
    }

    @Override
    public DeviceConfirmRedis addDevice(DeviceConfirmRedis deviceConfirmRedis) {
        return redisRepository.save(deviceConfirmRedis);
    }

    @Override
    public DeviceConfirmRedis findById(String id) {
        Optional<DeviceConfirmRedis> optional = redisRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public DeviceConfirmRedis deleteById(String id) {
        DeviceConfirmRedis device = this.findById(id);
        redisRepository.delete(device);
        return device;
    }

    @Override
    public void printTest(String print) {
    }
}
