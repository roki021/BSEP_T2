package com.admin.platform.repository;

import com.admin.platform.model.RevokedCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface RevokedCertificateRepository extends JpaRepository<RevokedCertificate, Integer> {
    Optional<RevokedCertificate> findBySerialNumber(BigInteger serialNumber);
}
