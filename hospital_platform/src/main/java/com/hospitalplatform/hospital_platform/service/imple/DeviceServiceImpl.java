package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.CSRAutofillDataDTO;
import com.hospitalplatform.hospital_platform.dto.CertificateSigningRequestDTO;
import com.hospitalplatform.hospital_platform.dto.DeviceDTO;
import com.hospitalplatform.hospital_platform.exception.impl.InvalidAPIResponse;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.mapper.DeviceMapper;
import com.hospitalplatform.hospital_platform.models.Device;
import com.hospitalplatform.hospital_platform.repository.DeviceRepository;
import com.hospitalplatform.hospital_platform.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Value("${admin_platform.csr_request_url}")
    private String requestUrl;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    @Qualifier("ignoreSSLConfig")
    private RestTemplate ignoreSSLRestTemplate;

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

    @Override
    public CertificateSigningRequestDTO getDeviceCsr(DeviceDTO deviceDTO,
                                                     CSRAutofillDataDTO autofillData) {
        return new CertificateSigningRequestDTO(
                deviceDTO.getIpAddress() + ":" + deviceDTO.getPort(),
                autofillData.getSurname(),
                autofillData.getGivenName(),
                autofillData.getOrganization(),
                autofillData.getOrganizationUnit(),
                autofillData.getCountry(),
                autofillData.getEmail(),
                deviceDTO.getId().toString(),
                "DEVICE"
        );
    }

    @Override
    public void forwardRequest(CertificateSigningRequestDTO csrDto, boolean isNew) throws InvalidAPIResponse {
        try {
            HttpEntity<byte[]> request = new HttpEntity<>(connectWithDevice(csrDto, isNew));
            HttpStatus httpStatus = ignoreSSLRestTemplate.exchange(
                    requestUrl,
                    HttpMethod.POST,
                    request,
                    String.class).getStatusCode();
        } catch (HttpClientErrorException exception) {
            throw new InvalidAPIResponse("Invalid API response.");
        }
    }

    private byte[] connectWithDevice(CertificateSigningRequestDTO csrDto, boolean isNew) throws InvalidAPIResponse {
        HttpEntity<CertificateSigningRequestDTO> request = new HttpEntity<>(csrDto);
        RestTemplate restTemplate;
        String endpoint;
        if(isNew) {
            restTemplate = new RestTemplate();
            endpoint = "http://" + csrDto.getCommonName() + "/csr";
        }

        else {
            restTemplate = ignoreSSLRestTemplate;
            endpoint = "https://" + csrDto.getCommonName() + "/csr";
        }
        try {
            ResponseEntity<byte[]> resp = restTemplate.exchange(
                    endpoint,
                    HttpMethod.POST,
                    request,
                    byte[].class);

            resp.getStatusCode();
            return resp.getBody();
        } catch (HttpClientErrorException exception) {
            throw new InvalidAPIResponse("Invalid API response.");
        }
    }
}
