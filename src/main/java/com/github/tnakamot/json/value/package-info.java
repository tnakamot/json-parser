/**
 * This package provides classes to represent JSON values in Java based on <a
 * href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>. All value types (object, array, number,
 * string, boolean and null) are supported.
 *
 * <p>The key class is {@link com.github.tnakamot.json.value.JSONValue}. This is a root class of
 * other classes that represent various types of JSON values. Actual representation of JSON values
 * is achieved by subclasses of {@link com.github.tnakamot.json.value.JSONValue}.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
package com.github.tnakamot.json.value;
