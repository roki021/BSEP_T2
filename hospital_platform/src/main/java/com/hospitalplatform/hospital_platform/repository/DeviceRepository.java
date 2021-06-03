package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    Device findByIpAddressAndPort(String ipAddress, int port);
}
