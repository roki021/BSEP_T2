package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;

import java.util.List;

public interface DeviceService {
    List<DeviceDTO> getAll();

    DeviceDTO addDevice(DeviceDTO deviceDTO) throws SQLConflict;
}
