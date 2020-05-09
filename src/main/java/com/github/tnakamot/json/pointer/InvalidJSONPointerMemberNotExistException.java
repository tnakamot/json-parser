package com.github.tnakamot.json.pointer;

/** Thrown when a member with the specified name does not exist. */
public class InvalidJSONPointerMemberNotExistException
    extends InvalidJSONPointerWithTokenException {
  public InvalidJSONPointerMemberNotExistException(JSONPointerReferenceToken token) {
    super(
        (token.previous() == null
                ? "The root JSON object"
                : "The JSON object '" + token.parent() + "'")
            + " does not have a member '"
            + token.name()
            + "'.",
        token);
  }
}
