package com.hospitalplatform.hospital_platform.mercury.message;

import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import com.hospitalplatform.hospital_platform.service.MessageService;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.RulesEngineBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class MessageBroker {
    @Autowired
    private MessageService messageService;

    private RulesEngine engine;
    private Rules rules;
    private Facts facts;
    private Long newEra;

    public MessageBroker() {
        this.engine = RulesEngineBuilder.aNewRulesEngine().withSilentMode(true).build(); // new DefaultRulesEngine();
        this.rules = new Rules();
        this.facts = new Facts();

        LocalDateTime datetime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        this.newEra = datetime.atZone(zoneId).toEpochSecond();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //System.out.println(simpleDateFormat.format(new Date()));
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formatDateTime = datetime.format(format);
        System.out.println("After Formatting: " + formatDateTime);
    }

    public void writeMessage(Message message) {
        messageService.saveDeviceMessage(message);
        this.facts.put("log", message);
        this.engine.fire(this.rules, this.facts);
    }

    public void addAlarm(Alarm alarm) {
        alarm.setNewEra(this.newEra);
        this.rules.register(alarm);
    }

    public void removeAlarm() {
        //TODO remove
    }
}
