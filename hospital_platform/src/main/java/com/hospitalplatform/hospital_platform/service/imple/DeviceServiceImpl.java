package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.mapper.DeviceMapper;
import com.hospitalplatform.hospital_platform.models.Device;
import com.hospitalplatform.hospital_platform.repository.DeviceRepository;
import com.hospitalplatform.hospital_platform.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    private DeviceMapper deviceMapper;

    public DeviceServiceImpl() {
        deviceMapper = new DeviceMapper();
    }

    @Override
    public List<DeviceDTO> getAll() {
        return deviceMapper.toDTOs(deviceRepository.findAll());
    }

    @Override
    public DeviceDTO addDevice(DeviceDTO deviceDTO) throws SQLConflict {
        if(deviceRepository.findByIpAddressAndPort(
                deviceDTO.getIpAddress(),
                deviceDTO.getPort()) != null) {
            throw new SQLConflict("Already registered device");
        }
        return deviceMapper.toDTO(
                deviceRepository.save(deviceMapper.toModel(deviceDTO)));
    }
}
