package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.models.HospitalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalUserRepository extends JpaRepository<HospitalUser, Long> {
    HospitalUser findByUsername(String username);
}
