package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONTokenBoolean;

/**
 * Represents one JSON 'boolean' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueBoolean extends JSONValuePrimitive {
    private final boolean value;

    /**
     * Create an instance of a Java representation of a JSON boolean value.
     *
     * @param value value represented by Java boolean primitive of this
     *              JSON boolean value.
     */
    public JSONValueBoolean(boolean value) {
        super(JSONValueType.BOOLEAN, null);
        this.value = value;
    }

    /**
     * Create an instance of a Java representation of a JSON boolean value
     * from a JSON boolean token.
     *
     * @param token source token of this JSON boolean value.
     */
    public JSONValueBoolean(JSONTokenBoolean token) {
        super(JSONValueType.BOOLEAN, token);
        this.value = token.value();
    }

    /**
     * Return the value represented by Java boolean primitives of this
     * JSON boolean value.
     *
     * @return value represented by Java boolean primitives of this JSON boolean value
     */
    public boolean value() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}