package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.LogSimulatorLogger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import com.hospitalplatform.hospital_platform.service.LoggerDemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class LoggerDemonServiceImpl implements LoggerDemonService {
    private MessageBroker messageBroker;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public LoggerDemonServiceImpl(MessageBroker broker) {
        this.messageBroker = broker;
        this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        this.threadPoolTaskScheduler.setPoolSize(5);
        this.threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        this.threadPoolTaskScheduler.initialize();

        this.addLogger(new LogSimulatorLogger(
                1,
                "..\\logger\\log1.txt",
                new LinkedHashMap<>() {{
                    put("username", "[a-zA-Z0-9]{3,50}");
                    put("ip", "[0-9.:]+");
                }}));
    }

    public void addLogger(Logger logger) {
        logger.subscribeBroker(this.messageBroker);
        this.threadPoolTaskScheduler.scheduleAtFixedRate(logger, logger.getReadPeriod() * 1000L);
    }
}
