package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.models.HospitalUser;
import com.hospitalplatform.hospital_platform.repository.HospitalUserRepository;
import com.hospitalplatform.hospital_platform.service.HospitalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalUserServiceImpl implements HospitalUserService {
    @Autowired
    private HospitalUserRepository repository;

    @Override
    public HospitalUser getUser(String username) {
        return repository.findByUsername(username);
    }
}
