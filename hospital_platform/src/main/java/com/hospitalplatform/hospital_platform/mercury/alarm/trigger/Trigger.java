package com.hospitalplatform.hospital_platform.mercury.alarm.trigger;


import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;

import javax.persistence.*;

@Entity
public class Trigger {
    @Id
    @GeneratedValue
    private int id;

    @Column
    private Double minValue;

    @Column
    private Double maxValue;

    @Column
    private String value;

    @Column
    private Relation relation;

    public Trigger() {
    }

    public Trigger(double minValue, double maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public Trigger(Relation relation, String value) {
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
                    return val.contains(input);
                default:
                    throw new Exception("Invalid relation for two Strings.");
            }
        }
        /*
        if (value instanceof Number) {
            Double val = Double.valueOf(value.toString());
            if (this.relation == Relation.EQ)
                return val.equals(this.value);
            else if (this.relation == Relation.LT)
                return val < (Double)this.value;
            else if (this.relation == Relation.GT)
                return val > (Double)this.value;
            else
                return val >= this.minValue && val <= this.maxValue;
        } else if (value instanceof String) {
            if (!relation.equals(Relation.EQ))
                return false;
            return value.toString().equals(this.value.toString());
        }

        return false;*/
    }

    public Object getValue() {
        return value;
    }
}
