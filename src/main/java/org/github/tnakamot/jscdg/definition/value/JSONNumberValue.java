package org.github.tnakamot.jscdg.definition.value;

public class JSONNumberValue extends JSONValue {
    private final Double value;
    public JSONNumberValue(Object obj) { this.value = (Double) obj; }
    public Double getValue() { return value; }

    @Override
    public String toString() { return value.toString(); }
}
