package com.admin.platform.repository;

import com.admin.platform.model.CertificateSigningRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateSigningRequestRepository extends JpaRepository<CertificateSigningRequest, String> {
}
