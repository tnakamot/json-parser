package org.github.tnakamot.jscdg.definition.value;

import org.json.simple.JSONArray;

public class JSONNumberArrayValue extends JSONValue {
    private final Double[] value;
    public JSONNumberArrayValue(Object obj) {
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            this.value = new Double[array.size()];
            for (int i=0; i<array.size(); i++) {
                this.value[i] = (Double) array.get(i);
            }
        } else {
            this.value = new Double[]{ (Double) obj };
        }
    }
    public Double[] getValue() { return value.clone(); }

    @Override
    public String toString() { return String.join(",", value.toString()); }
}
