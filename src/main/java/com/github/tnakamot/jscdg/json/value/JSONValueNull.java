package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONToken;

/**
 * Represents one JSON 'null' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueNull extends JSONValuePrimitive {
    /**
     * Create an instance of a Java representation of a JSON null value.
     */
    public JSONValueNull() {
        this(null);
    }

    /**
     * Create an instance of a Java representation of a JSON null value
     * with source JSON text information.
     *
     * @param token source of this JSON null value. Can be null if this JSON
     *              null value is not originated from an exiting JSON text.
     */
    public JSONValueNull(JSONToken token) {
        super(JSONValueType.NULL, token);
    }

    @Override
    public String toString() {
        return "null";
    }
}
