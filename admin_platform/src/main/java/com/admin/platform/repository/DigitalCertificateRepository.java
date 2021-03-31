package com.admin.platform.repository;

import com.admin.platform.model.DigitalCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface DigitalCertificateRepository extends JpaRepository<DigitalCertificate, Integer> {
    DigitalCertificate findBySerialNumber(BigInteger serialNumber);
}
