package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    Optional<Alarm> findByName(String name);
}
