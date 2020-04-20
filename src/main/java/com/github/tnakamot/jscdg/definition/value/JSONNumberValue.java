package com.github.tnakamot.jscdg.definition.value;

public class JSONNumberValue extends JSONValue {
    private final Double value;
    public JSONNumberValue(Object obj) {
        if (obj instanceof Long) {
            Long l = (Long) obj;
            this.value = Double.valueOf(l.doubleValue());
        } else {
            this.value = (Double) obj;
        }
    }
    public Double getValue() { return value; }

    @Override
    public String toString() { return value.toString(); }
}
