package com.hospitalplatform.hospital_platform.mercury.alarm;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String message;

    @Column
    private Timestamp time;

    @Column
    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "alarm_id")
    private Alarm alarm;

    public Notification() {}

    public Notification(String message, Timestamp time, Alarm alarm) {
        this.message = message;
        this.time = time;
        this.checked = false;
        this.alarm = alarm;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }
}
