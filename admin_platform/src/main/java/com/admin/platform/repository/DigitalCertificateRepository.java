package com.admin.platform.repository;

import com.admin.platform.model.DigitalCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface DigitalCertificateRepository extends JpaRepository<DigitalCertificate, Integer> {
    Optional<DigitalCertificate> findBySerialNumber(BigInteger serialNumber);
}
