package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONToken;

/**
 * Represents one JSON primitive value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public abstract class JSONValuePrimitive extends JSONValue {
    private final JSONToken token;

    /**
     * Create an instance of a Java representation of a JSON primitive value.
     *
     * @param type type of this JSON value
     */
    JSONValuePrimitive(JSONValueType type) {
        this(type, null);
    }

    /**
     * Create an instance of a Java representation of a JSON primitive value
     * with source JSON text information.
     *
     * @param type  type of this JSON value
     * @param token source of this JSON value. Can be null if this JSON value
     *              is not originated from an exiting JSON text.
     */
    JSONValuePrimitive(JSONValueType type, JSONToken token) {
        super(type);
        this.token = token;
    }


    /**
     * The source token of this JSON value.
     *
     * @return the source token of this JSON value. Can be null if this JSON value
     *         is not originated from an existing JSON text.
     */
    public JSONToken token() {
        return token;
    }
}
