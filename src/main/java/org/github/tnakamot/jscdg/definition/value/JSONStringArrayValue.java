package org.github.tnakamot.jscdg.definition.value;

import org.json.simple.JSONArray;

public class JSONStringArrayValue extends JSONValue {
    private final String[] value;
    public JSONStringArrayValue(Object obj) {
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            this.value = new String[array.size()];
            for (int i=0; i<array.size(); i++) {
                this.value[i] = (String) array.get(i);
            }
        } else {
            this.value = new String[]{ (String) obj };
        }
    }
    public String[] getValue() { return value.clone(); }

    @Override
    public String toString() { return String.join(",", value); }
}
