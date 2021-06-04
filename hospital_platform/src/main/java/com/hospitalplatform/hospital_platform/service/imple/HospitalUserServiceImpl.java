package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.HospitalUserDTO;
import com.hospitalplatform.hospital_platform.dto.NewMemberDTO;
import com.hospitalplatform.hospital_platform.dto.RoleUpdateDTO;
import com.hospitalplatform.hospital_platform.mapper.HospitalUserMapper;
import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.models.Role;
import com.hospitalplatform.hospital_platform.repository.HospitalUserRepository;
import com.hospitalplatform.hospital_platform.repository.RoleRepository;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HospitalUserServiceImpl implements HospitalUserService {
    @Autowired
    private HospitalUserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public HospitalUser getUser(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public void createUser(NewMemberDTO member) throws Exception {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); //TODO: autowired doesn't work for some reason
        List<Role> roles = new ArrayList<>();

        if (member.getRole().equals("doctor"))
            roles.add(roleRepository.findByName("ROLE_DOCTOR").get());
        else if (member.getRole().equals("admin"))
            roles.add(roleRepository.findByName("ROLE_ADMIN").get());
        else
            throw new Exception("Invalid user role.");

        repository.save(
                new HospitalUser(
                        member.getUsername(),
                        member.getFirstName(),
                        member.getLastName(),
                        passwordEncoder.encode(member.getPassword()),
                        member.getEmail(),
                        roles
                )
        );
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException exception) {
        }
    }

    @Override
    public void changeUserRole(Integer userId, @Validated RoleUpdateDTO roleUpdateDTO) {
        Optional<HospitalUser> hospitalUserOptional = repository.findById(userId);
        if (hospitalUserOptional.isEmpty())
            return;

        HospitalUser hospitalUser = hospitalUserOptional.get();
        Role newRole = null;

        if (roleUpdateDTO.getRole().equals("admin"))
            newRole = roleRepository.findByName("ROLE_ADMIN").get();
        else if (roleUpdateDTO.getRole().equals("doctor"))
            newRole = roleRepository.findByName("ROLE_DOCTOR").get();

        hospitalUser.getRoles().clear();
        hospitalUser.getRoles().add(newRole);
        repository.save(hospitalUser);
    }

    @Override
    public List<HospitalUserDTO> getHospitalUsers() {
        HospitalUserMapper mapper = new HospitalUserMapper();
        return repository.findAll().stream().map(user -> mapper.getDTO(user)).collect(Collectors.toList());
    }


}
