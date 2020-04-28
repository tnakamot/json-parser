package com.github.tnakamot.jscdg.json.value;

/**
 * Represents a type of a JSON value.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259#section-3">RFC 8259 - 3. Values</a>
 */
public enum JSONValueType {
    STRING,
    NUMBER,
    BOOLEAN,
    NULL,
    OBJECT,
    ARRAY
}
