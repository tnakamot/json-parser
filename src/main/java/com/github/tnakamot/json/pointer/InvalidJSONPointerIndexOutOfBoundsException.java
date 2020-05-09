package com.github.tnakamot.json.pointer;

/** Thrown when the index in the JSON Pointer is out of bounds of the JSON array value. */
public class InvalidJSONPointerIndexOutOfBoundsException
    extends InvalidJSONPointerWithTokenException {
  public InvalidJSONPointerIndexOutOfBoundsException(JSONPointerReferenceToken token, int size) {
    super(
        "Index "
            + token.text()
            + " is out of bounds of the JSON array '"
            + token.parent()
            + "' (size: "
            + size
            + ").",
        token);
  }
}
