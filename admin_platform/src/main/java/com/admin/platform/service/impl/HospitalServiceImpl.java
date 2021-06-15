package com.admin.platform.service.impl;

import com.admin.platform.dto.*;
import com.admin.platform.mapper.HospitalMapper;
import com.admin.platform.model.Hospital;
import com.admin.platform.repository.HospitalRepository;
import com.admin.platform.service.HospitalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import javax.management.relation.Role;
import java.nio.charset.StandardCharsets;
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

    @Autowired
    private JavaMailSender mailSender;

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
        HttpHeaders headers = new HttpHeaders();
        headers.add("origin", "https://localhost:8080");
        headers.add("X-sudo-key", hospitalDTO.get().getCommunicationToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        ResponseEntity<HospitalUserDTO[]> responseEntity =
                restTemplate.exchange(requestUrl, HttpMethod.GET, entity, HospitalUserDTO[].class);

        if (responseEntity.getStatusCodeValue() != 200)
            throw new Exception("Unable to get hospital members.");

        return Arrays.asList(responseEntity.getBody());
    }

    @Override
    public void addHospitalMember(Integer hospitalId, NewMemberDTO memberDTO) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        //TODO: check rules and privileges!!!

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-sudo-key", hospitalDTO.get().getCommunicationToken());
        headers.add("origin", "https://localhost:8080");
        HttpEntity<NewMemberDTO> request = new HttpEntity<>(memberDTO, headers);
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
        HttpHeaders headers = new HttpHeaders();
        headers.add("origin", "https://localhost:8080");
        headers.add("X-sudo-key", hospitalDTO.get().getCommunicationToken());
        HttpEntity<String> entity = new HttpEntity<>("body", headers);
        restTemplate.exchange(requestUrl, HttpMethod.DELETE, entity, Object.class);
    }

    @Override
    public void changeHospitalMemberRole(
            Integer hospitalId, Integer memberId, @Validated RoleUpdateDTO newRole) throws Exception {
        Optional<Hospital> hospitalDTO = hospitalRepository.findById(hospitalId);

        if (hospitalDTO.isEmpty())
            throw new Exception("Hospital does not exist.");

        HttpHeaders headers = new HttpHeaders();
        headers.add("origin", "https://localhost:8080");
        headers.add("X-sudo-key", hospitalDTO.get().getCommunicationToken());
        HttpEntity<RoleUpdateDTO> request = new HttpEntity<>(newRole, headers);
        String requestUrl = String.format(
                "http://%s/api/external/members/%d/roles", hospitalDTO.get().getEndpoint(), memberId);
        restTemplate.put(requestUrl, request);
    }

    @Override
    public void sendLoggerConfigurationToAdministration(Integer hospitalId, @Validated LoggersDTO loggersDTO) throws Exception {
        Optional<Hospital> hospital = hospitalRepository.findById(hospitalId);

        if (hospital.isEmpty())
            throw new Exception("No email.");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String loggers = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loggersDTO);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                helper.setFrom("cultoffer@gmail.com");
                helper.setTo(hospital.get().getAdministrationEmail());
                helper.setSubject("Logger simulator configuration");
                helper.setText("Here is your logger simulator configuration.");
                helper.addAttachment("logger_config.json",
                        new ByteArrayResource(loggers.getBytes(StandardCharsets.UTF_8)));
                mailSender.send(mimeMessage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
