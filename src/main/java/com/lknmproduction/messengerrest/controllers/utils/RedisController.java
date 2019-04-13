package com.lknmproduction.messengerrest.controllers.utils;

import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import com.lknmproduction.messengerrest.service.redis.ConfirmPinCodeRedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RedisController.BASE_URL)
public class RedisController {

    public static final String BASE_URL = "/api/v1/redis";

    private final ConfirmPinCodeRedisService redisService;

    public RedisController(ConfirmPinCodeRedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public DeviceConfirmRedis getRedisValue(@PathVariable String id) {
        return redisService.findById(id);
    }

}
