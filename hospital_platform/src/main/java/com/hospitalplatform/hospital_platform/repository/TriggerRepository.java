package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerRepository extends JpaRepository<Trigger, Long> {
}
