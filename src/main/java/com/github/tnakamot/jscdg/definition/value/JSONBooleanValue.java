package com.github.tnakamot.jscdg.definition.value;

public class JSONBooleanValue extends JSONValue {
    private final Boolean value;
    public JSONBooleanValue(Object obj) { this.value = (Boolean) obj; }
    public Boolean getValue() { return value; }

    @Override
    public String toString() { return value.toString(); }
}
