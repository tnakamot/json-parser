package com.github.tnakamot.jscdg.json.value;

/**
 * Represents one JSON value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public abstract class JSONValue {
    private final JSONValueType type;

    /**
     * Create an instance of a Java representation of a JSON value.
     *
     * @param type type of this JSON value
     */
    JSONValue(JSONValueType type) {
        this.type = type;
    }

    /**
     * Type of this JSON value.
     *
     * @return type of this JSON value
     */
    public JSONValueType type() {
        return type;
    }
}
