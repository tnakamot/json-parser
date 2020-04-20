package com.github.tnakamot.jscdg.definition.value;

import org.json.simple.JSONArray;

public class JSONBooleanArrayValue extends JSONValue {
    private final Boolean[] value;
    public JSONBooleanArrayValue(Object obj) {
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            this.value = new Boolean[array.size()];
            for (int i=0; i<array.size(); i++) {
                this.value[i] = (Boolean) array.get(i);
            }
        } else {
            this.value = new Boolean[]{ (Boolean) obj };
        }
    }
    public Boolean[] getValue() { return value.clone(); }

    @Override
    public String toString() { return String.join(",", value.toString()); }
}
