package com.hospitalplatform.hospital_platform.mercury.alarm.trigger;


import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;

import javax.persistence.*;

@Entity
public class Trigger {
    @Id
    //@GeneratedValue
    private Long id;

    @Column
    private Double minValue;

    @Column
    private Double maxValue;

    @Column
    private String value;

    @Column
    private Relation relation;

    public void setId(Long id) {
        this.id = id;
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

    public Relation getRelation() {
        return relation;
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Trigger() {
    }

    public Trigger(Long id, double minValue, double maxValue) {
        this.id = id;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Trigger(Long id, Relation relation, String value) {
        this.id = id;
        this.value = value;
        this.relation = relation;
    }

    public boolean check(Object value) throws Exception {
        try
        {
            double input = Double.parseDouble(value.toString());
            double val = Double.parseDouble(this.value.toString());

            switch (this.relation) {
                case EQ:
                    return val == input;
                case LT:
                    return val < input;
                case GT:
                    return val > input;
                case CONTAINS:
                    throw new Exception("Invalid relation for two numbers.");
                default:
                    return minValue <= val && val <= maxValue;
            }
        }
        catch(NumberFormatException e)
        {
            String input = value.toString();
            String val = this.value;

            switch (this.relation) {
                case EQ:
                    return val.equals(input);
                case CONTAINS:
                    System.out.println("> " + val);
                    return val.contains(input);
                default:
                    throw new Exception("Invalid relation for two Strings.");
            }
        }
    }

    public Object getValue() {
        return value;
    }

    public Long getId() {
        return id;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
