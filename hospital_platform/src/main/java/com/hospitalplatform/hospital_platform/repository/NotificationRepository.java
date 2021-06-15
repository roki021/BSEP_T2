package com.hospitalplatform.hospital_platform.repository;

import com.hospitalplatform.hospital_platform.mercury.alarm.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
