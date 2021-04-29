package com.admin.platform.repository;

import com.admin.platform.model.CertificateSigningRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateSigningRequestRepository extends JpaRepository<CertificateSigningRequest, Long> {
    Optional<CertificateSigningRequest> findById(Long id);
    List<CertificateSigningRequest> findByActive(boolean active);
}
