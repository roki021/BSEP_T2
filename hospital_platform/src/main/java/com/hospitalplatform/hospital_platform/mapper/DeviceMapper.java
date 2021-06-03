package com.hospitalplatform.hospital_platform.mapper;

import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.models.Device;

import java.util.List;
import java.util.stream.Collectors;

public class DeviceMapper {
    public Device toModel(DeviceDTO deviceDTO) {
        return new Device(
                deviceDTO.getCommonName(),
                deviceDTO.getIpAddress(),
                deviceDTO.getPort()
        );
    }

    public DeviceDTO toDTO(Device device) {
        return new DeviceDTO(
                device.getId(),
                device.getCommonName(),
                device.getIpAddress(),
                device.getPort()
        );
    }

    public List<DeviceDTO> toDTOs(List<Device> devices) {
        return devices.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
