package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, String> {

    @Query("SELECT d.pushId FROM Device d WHERE d.isActive = true")
    List<String> findAllPushIds();

    @Query("UPDATE Device d SET d.pushId = ?2 WHERE d.id = ?1")
    @Modifying
    void setPushIdToDevice(String deviceId, String pushId);

    @Query("UPDATE Device d SET d.isActive = ?2 WHERE d.id = ?1")
    @Modifying
    void setDeviceIsActive(String deviceId, boolean isActive);
}
