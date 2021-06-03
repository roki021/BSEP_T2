package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.mapper.HospitalUserMapper;
import com.hospitalplatform.hospital_platform.models.HospitalAdmin;
import com.hospitalplatform.hospital_platform.models.HospitalDoctor;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.repository.HospitalUserRepository;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HospitalUserServiceImpl implements HospitalUserService {
    @Autowired
    private HospitalUserRepository repository;

    @Override
    public HospitalUser getUser(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void createUser(NewMemberDTO member) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //TODO: autowired doesn't work for some reason
        if (member.getRole().equals("doctor"))
            repository.save(
                    new HospitalDoctor(
                        member.getUsername(),
                        member.getFirstName(),
                        member.getLastName(),
                        passwordEncoder.encode(member.getPassword()),
                        member.getEmail()
                    ));
        else if (member.getRole().equals("admin"))
            repository.save(
                    new HospitalAdmin(
                        member.getUsername(),
                        member.getFirstName(),
                        member.getLastName(),
                        passwordEncoder.encode(member.getPassword()),
                        member.getEmail()
                    ));
        else
            throw new Exception("Invalid user role.");
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
        }
    }

    @Override
    public List<HospitalUserDTO> getHospitalUsers() {
        HospitalUserMapper mapper = new HospitalUserMapper();
        return repository.findAll().stream().map(user -> mapper.getDTO(user)).collect(Collectors.toList());
    }
}
