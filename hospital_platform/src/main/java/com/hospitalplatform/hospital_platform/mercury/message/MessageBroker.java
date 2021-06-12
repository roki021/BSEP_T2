package com.hospitalplatform.hospital_platform.mercury.message;

import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.RulesEngineBuilder;

public class MessageBroker {
    private RulesEngine engine;
    private Rules rules;
    private Facts facts;

    public MessageBroker() {
        this.engine = RulesEngineBuilder.aNewRulesEngine().withSilentMode(true).build(); // new DefaultRulesEngine();
        this.rules = new Rules();
        this.facts = new Facts();
    }

    public void writeMessage(Message message) {
        this.facts.put("log", message);
        this.engine.fire(this.rules, this.facts);
    }

    public void addAlarm(Alarm alarm) {
        this.rules.register(alarm);
    }

    public void removeAlarm() {
        //TODO remove
    }
}
