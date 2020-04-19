package org.github.tnakamot.jscdg.definition.value;

public class JSONIntegerValue extends JSONValue {
    private final Long value;
    public JSONIntegerValue(Object obj) { this.value = (Long) obj; }
    public Long getValue() { return value; }

    @Override
    public String toString() { return value.toString(); }
}
