package com.hospitalplatform.hospital_platform.dto;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;

public class TriggerDTO {

    private int id;
    private Double minValue;
    private Double maxValue;
    private String value;
    private Relation relation;

    public int getId() {
        return id;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }
}
