package com.lknmproduction.messengerrest.repositories.redis;

import com.lknmproduction.messengerrest.domain.redis.DeviceConfirmRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<DeviceConfirmRedis, String> {
}
