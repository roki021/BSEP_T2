package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.LogSimulatorLogger;

public interface LoggerDemonService {
    void addLogger(Logger logger);
}
