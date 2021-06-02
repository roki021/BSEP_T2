package com.admin.platform.service.impl;

import com.admin.platform.dto.HospitalDTO;
import com.admin.platform.dto.HospitalUserDTO;
import com.admin.platform.dto.NewMemberDTO;
import com.admin.platform.mapper.HospitalMapper;
import com.admin.platform.model.Hospital;
import com.admin.platform.repository.HospitalRepository;
import com.admin.platform.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

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

        RestTemplate restTemplate = new RestTemplate();
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

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<NewMemberDTO> request = new HttpEntity<>(memberDTO);
        String requestUrl = String.format("http://%s/api/external/members", hospitalDTO.get().getEndpoint());
        restTemplate.postForLocation(requestUrl, request);
    }

    @Override
    public void changeHospitalMemberRole(Integer hospitalId, Integer memberId, Object newRole) {

    }
}