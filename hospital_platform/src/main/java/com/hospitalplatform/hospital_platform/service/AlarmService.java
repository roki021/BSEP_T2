package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;

import java.util.List;

public interface AlarmService {

    AlarmDTO addAlarm(AlarmDTO alarmDTO) throws SQLConflict;

    void removeAlarm(Long id) throws UnexpectedSituation;

    List<AlarmDTO> getAll();
}
