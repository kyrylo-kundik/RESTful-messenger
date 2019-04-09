package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query("SELECT d.pushId FROM Device d")
    List<String> findAllPushIds();

}
