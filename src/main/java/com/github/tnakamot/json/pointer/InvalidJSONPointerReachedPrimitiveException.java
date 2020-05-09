package com.github.tnakamot.json.pointer;

/**
 * Thrown when a JSON Pointer reaches a primitive JSON value (null, boolean, string or number), but
 * it references a child of that value.
 */
public class InvalidJSONPointerReachedPrimitiveException
    extends InvalidJSONPointerWithTokenException {
  public InvalidJSONPointerReachedPrimitiveException(JSONPointerReferenceToken token) {
    super(
        "Parent pointer '" + token.parent() + "' is a reference to a primitive JSON value.", token);
  }
}
