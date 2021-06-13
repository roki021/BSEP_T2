package com.hospitalplatform.hospital_platform.config;

import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.AuthLogger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.LogSimulatorLogger;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import com.hospitalplatform.hospital_platform.repository.AlarmRepository;
import com.hospitalplatform.hospital_platform.service.LoggerDemonService;
import com.hospitalplatform.hospital_platform.service.imple.LoggerDemonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Optional;

@Configuration
public class MercuryConfig {
    public static String LOGIN_ALARM = "LOGIN_ALARM";
    public static String IP_BLACKLIST_ALARM = "IP_BLACKLIST_ALARM";
    public static String LONG_NO_ACTIVE_ALARM = "LONG_NO_ACTIVE_ALARM";
    public static String ERROR_ALARM = "ERROR_ALARM";
    public static String DOS_ALARM = "DOS_ALARM";


    @Autowired
    private AlarmRepository alarmRepository;

    @Bean
    public MessageBroker createMessageBrokerInstance() {
        MessageBroker messageBroker = new MessageBroker();

        Optional<Alarm> loginAlarmOptional = this.alarmRepository.findByName(LOGIN_ALARM);
        Optional<Alarm> ipBlackListAlarmOptional = this.alarmRepository.findByName(IP_BLACKLIST_ALARM);
        Optional<Alarm> longNoActiveAlarmOptional = this.alarmRepository.findByName(LONG_NO_ACTIVE_ALARM);
        Optional<Alarm> errorAlarmOptional = this.alarmRepository.findByName(ERROR_ALARM);
        Optional<Alarm> dosAlarmOptional = this.alarmRepository.findByName(DOS_ALARM);

        if (dosAlarmOptional.isEmpty()) {
            // security, dos alarm
            Alarm dosAlarm = new Alarm(
                    MercuryConfig.DOS_ALARM,
                    "System is under DoS attack.",
                    (alarm) -> {
                        System.out.println(alarm.message());
                    },
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag() | ActivationTag.LOG_SIMULATOR.getTag(),
                    new LinkedHashMap<>() {{
                        put("status", new Trigger(Relation.CONTAINS, "SUCCESS,ERROR,INFO,WARN"));
                    }},
                    60, 1L);
            this.alarmRepository.save(dosAlarm);
            messageBroker.addAlarm(dosAlarm);
        } else
            messageBroker.addAlarm(dosAlarmOptional.get());

        if (errorAlarmOptional.isEmpty()) {
            // security, detect any error in the system
            Alarm errorAlarm = new Alarm(
                    MercuryConfig.ERROR_ALARM,
                    "Error pops up!",
                    (alarm) -> {
                        System.out.println(alarm.message());
                    },
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag() | ActivationTag.LOG_SIMULATOR.getTag(),
                    new LinkedHashMap<>() {{
                        put("status", new Trigger(Relation.EQ, "ERROR"));
                    }},
                    1, Long.MAX_VALUE);
            this.alarmRepository.save(errorAlarm);
            messageBroker.addAlarm(errorAlarm);
        } else
            messageBroker.addAlarm(errorAlarmOptional.get());

        if (longNoActiveAlarmOptional.isEmpty()) {
            // security alarm
            Alarm longNoActiveAlarm = new Alarm(
                    MercuryConfig.LONG_NO_ACTIVE_ALARM,
                    "User active again after 90 days. (username %s)",
                    (alarm) -> {
                        System.out.println(alarm.message());
                    },
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag(),
                    new LinkedHashMap<>() {{
                        put("status", new Trigger(Relation.EQ, "SUCCESS")); //TODO: id = active??
                    }},
                    1, 10L, "username"); //TODO: fact wait 90 days!
            this.alarmRepository.save(longNoActiveAlarm);
            messageBroker.addAlarm(longNoActiveAlarm);
        } else
            messageBroker.addAlarm(loginAlarmOptional.get());

        if (loginAlarmOptional.isEmpty()) {
            // security alarms
            Alarm loginAlarm = new Alarm(
                    MercuryConfig.LOGIN_ALARM,
                    "User is failing to login for many times. (username %s)",
                    (alarm) -> {
                        System.out.println(alarm.message());
                    },
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag(),
                    new LinkedHashMap<>() {{
                        put("status", new Trigger(Relation.EQ, "WARNING"));
                    }},
                    3, 3600L, "username");
            this.alarmRepository.save(loginAlarm);
            messageBroker.addAlarm(loginAlarm);
        } else
            messageBroker.addAlarm(loginAlarmOptional.get());

        if (ipBlackListAlarmOptional.isEmpty()) {
            // security alarms
            Alarm ipBlackListAlarm = new Alarm(
                    MercuryConfig.IP_BLACKLIST_ALARM,
                    "Access from black listed address. (ip %s)",
                    (alarm) -> {
                        System.out.println(alarm.message());
                    },
                    ActivationTag.SEC.getTag() | ActivationTag.LOG_SIMULATOR.getTag(),
                    new LinkedHashMap<>() {{
                        put("ip", new Trigger(Relation.CONTAINS, "127.0.0.1,127.0.0.2"));
                    }},
                    1, 3L, "ip");
            this.alarmRepository.save(ipBlackListAlarm);
            messageBroker.addAlarm(ipBlackListAlarm);
        } else
            messageBroker.addAlarm(ipBlackListAlarmOptional.get());

        return messageBroker;
    }

    @Bean(name = "authLogger")
    public Logger createAuthLoggerInstance() {
        AuthLogger authLogger = new AuthLogger(new LinkedHashMap<>() {{
            put("username", "[a-zA-Z0-9]{3,50}");
            put("ip", "[0-9.:]+");
        }});
        authLogger.subscribeBroker(this.createMessageBrokerInstance());
        return authLogger;
    }
}
