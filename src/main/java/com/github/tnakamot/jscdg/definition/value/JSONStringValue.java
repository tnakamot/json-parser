package com.github.tnakamot.jscdg.definition.value;

public class JSONStringValue extends JSONValue {
    private final String value;
    public JSONStringValue(Object obj) { this.value = (String) obj; }
    public String getValue() { return value; }

    @Override
    public String toString() { return value; }
}
