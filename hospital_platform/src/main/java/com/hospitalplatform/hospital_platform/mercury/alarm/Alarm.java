package com.hospitalplatform.hospital_platform.mercury.alarm;


import com.hospitalplatform.hospital_platform.mercury.alarm.callback.AlarmCallback;
import com.hospitalplatform.hospital_platform.mercury.alarm.state.Historian;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.core.BasicRule;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Alarm extends BasicRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private int activationTags;

    @Column
    private int activationThreshold;

    @Column
    private String messageTracker;

    @MapKey(name = "id")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true ,  mappedBy = "id")
    private Map<String, Trigger> triggers;

    @Transient
    private Historian historian;

    @Column
    private String alarmMessage;

    @Column
    private String[] triggerNames;

    @Column
    private String lastMessageKey;

    @Transient
    private AlarmCallback callback; //TODO: if null just write to NoSQL?!

    public Alarm() {
    }

    public Alarm(String name, String alarmMessage, AlarmCallback alarmCallback, int activationTags, HashMap<String, Trigger> triggers) {
        this(name, alarmMessage, alarmCallback, activationTags, triggers, 1, Long.MAX_VALUE, null);
    }

    public Alarm(
            String name,
            String alarmMessage,
            AlarmCallback alarmCallback,
            int activationTags,
            HashMap<String, Trigger> triggers,
            int activationThreshold,
            Long factWait) {
        this(name, alarmMessage, alarmCallback, activationTags, triggers, 1, Long.MAX_VALUE, null, null);
    }

    public Alarm(
            String name,
            String alarmMessage,
            AlarmCallback alarmCallback,
            int activationTags,
            HashMap<String, Trigger> triggers,
            int activationThreshold,
            Long factWait,
            String messageTracker,
            String... triggersNames) {
        super(name);
        this.name = name;
        this.activationTags = activationTags;
        this.triggers = triggers;
        this.activationThreshold = activationThreshold;
        this.messageTracker = messageTracker;
        this.historian = new Historian(factWait);
        this.alarmMessage = alarmMessage;
        this.triggerNames = triggersNames;
        this.callback = alarmCallback;
    }

    @Override
    public boolean evaluate(Facts facts) {
        Message message = (Message) facts.get("log");
        if (!message.checkAccess(activationTags))
            return false;

        for (String triggerFieldName : this.triggers.keySet()) {
            try {
                if (!this.triggers.get(triggerFieldName).check(message.getField(triggerFieldName)))
                    return false;
            } catch (Exception e) {
                return false;
            }
        }

        String messageKey = message.getField(this.messageTracker) == null ?
                "[root]" : message.getField(this.messageTracker).toString();

        this.historian.update(messageKey, message.getTime());

        this.lastMessageKey = messageKey;

        return this.historian.getCount(messageKey) >= activationThreshold;
    }

    @Override
    public void execute(Facts facts) {
        if (this.callback != null)
            this.callback.execute(this);
        else
            System.err.println(this.message());
    }

    public String message() {
        List<String> newList = new ArrayList<>();
        for (String key : this.triggerNames)
            newList.add(this.triggers.get(key).getValue().toString());

        if (this.messageTracker != null)
            newList.add(this.lastMessageKey);
        return String.format(this.alarmMessage, newList.toArray());
    }
}