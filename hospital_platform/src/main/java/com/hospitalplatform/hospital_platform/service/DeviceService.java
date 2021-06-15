package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;

import java.util.List;

public interface DeviceService {
    List<DeviceDTO> getAll();

    DeviceDTO addDevice(DeviceDTO deviceDTO) throws SQLConflict;

    CertificateSigningRequestDTO getDeviceCsr(DeviceDTO deviceDTO,
                                              CSRAutofillDataDTO autofillData);

    void forwardRequest(CertificateSigningRequestDTO csrDto, boolean isNew) throws InvalidAPIResponse;

    boolean isValidToken(String token);
}
