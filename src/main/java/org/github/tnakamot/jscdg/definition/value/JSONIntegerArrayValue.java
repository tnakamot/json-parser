package org.github.tnakamot.jscdg.definition.value;

import org.json.simple.JSONArray;

public class JSONIntegerArrayValue extends JSONValue {
    private final Long[] value;
    public JSONIntegerArrayValue(Object obj) {
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            this.value = new Long[array.size()];
            for (int i=0; i<array.size(); i++) {
                this.value[i] = (Long) array.get(i);
            }
        } else {
            this.value = new Long[]{ (Long) obj };
        }
    }
    public Long[] getValue() { return value.clone(); }

    @Override
    public String toString() { return String.join(",", value.toString()); }
}
