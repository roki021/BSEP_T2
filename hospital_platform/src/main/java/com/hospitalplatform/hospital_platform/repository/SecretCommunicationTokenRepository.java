package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.models.SecretCommunicationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretCommunicationTokenRepository extends JpaRepository<SecretCommunicationToken, Integer> {
}
