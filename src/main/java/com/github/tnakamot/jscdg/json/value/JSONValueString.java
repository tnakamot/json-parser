package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONTokenString;

/**
 * Represents one JSON 'string' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueString extends JSONValuePrimitive {
    private final String value;

    /**
     * Create an instance of a Java representation of a JSON string value.
     *
     * @param value value represented by a Java {@link String} object of this
     *              JSON string value. Null is considered as an empty string.
     */
    public JSONValueString(String value) {
        super(JSONValueType.STRING, null);
        this.value = (value == null) ? "" : value;
    }

    /**
     * Create an instance of a Java representation of a JSON string value
     * from a JSON string token.
     *
     * @param token source token of this JSON string value.
     */
    public JSONValueString(JSONTokenString token) {
        super(JSONValueType.STRING, token);
        this.value = token.value();
    }

    /**
     * Return the value represented by a Java {@link String} object of this
     * JSON string value.
     *
     * @return value represented by a Java {@link String} object of this JSON
     *               string value. Never be null.
     */
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONValueString) {
            JSONValueString v = (JSONValueString) obj;
            return this.value.equals(v);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
