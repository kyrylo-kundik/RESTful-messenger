package com.lknmproduction.messengerrest.repositories;

import com.lknmproduction.messengerrest.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
