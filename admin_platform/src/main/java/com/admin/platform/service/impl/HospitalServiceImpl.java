package com.admin.platform.service.impl;

import com.admin.platform.dto.HospitalDTO;
import com.admin.platform.dto.HospitalUserDTO;
import com.admin.platform.dto.NewMemberDTO;
import com.admin.platform.dto.RoleUpdateDTO;
import com.admin.platform.mapper.HospitalMapper;
import com.admin.platform.model.Hospital;
import com.admin.platform.repository.HospitalRepository;
import com.admin.platform.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    @Qualifier("ignoreSSLConfig")
    private RestTemplate restTemplate;

    @Override
    public List<HospitalDTO> getHospitals() {
        HospitalMapper hospitalMapper = new HospitalMapper();
        return hospitalRepository.findAll().stream().map(
                hospital -> hospitalMapper.getDTO(hospital)).collect(Collectors.toList());
    }

    @Override
    public List<HospitalUserDTO> getHospitalMembers(Integer hospitalId) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        String requestUrl = String.format("http://%s/api/external/members", hospitalDTO.get().getEndpoint());
        ResponseEntity<HospitalUserDTO[]> responseEntity = restTemplate.getForEntity(
                requestUrl, HospitalUserDTO[].class);

        if (responseEntity.getStatusCodeValue() != 200)
            throw new Exception("Unable to get hospital members.");

        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public void addHospitalMember(Integer hospitalId, NewMemberDTO memberDTO) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        HttpEntity<NewMemberDTO> request = new HttpEntity<>(memberDTO);
        String requestUrl = String.format("http://%s/api/external/members", hospitalDTO.get().getEndpoint());
        restTemplate.postForLocation(requestUrl, request);
    }

    @Override
    public void deleteHospitalMember(Integer hospitalId, Integer memberId) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        String requestUrl = String.format(
                "http://%s/api/external/members/%d", hospitalDTO.get().getEndpoint(), memberId);
        restTemplate.delete(requestUrl);
    }

    @Override
    public void changeHospitalMemberRole(
            Integer hospitalId, Integer memberId, @Validated RoleUpdateDTO newRole) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        HttpEntity<RoleUpdateDTO> request = new HttpEntity<>(newRole);
        String requestUrl = String.format(
                "http://%s/api/external/members/%d/roles", hospitalDTO.get().getEndpoint(), memberId);
        restTemplate.put(requestUrl, request);
    }
}
