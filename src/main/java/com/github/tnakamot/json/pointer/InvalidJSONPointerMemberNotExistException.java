package com.github.tnakamot.json.pointer;

/** Thrown when a member with the specified name does not exist. */
public class InvalidJSONPointerMemberNotExistException
    extends InvalidJSONPointerWithTokenException {
  public InvalidJSONPointerMemberNotExistException(JSONPointerReferenceToken token) {
    super(
        "The JSON object '"
            + token.parent()
            + "' does not have a member with the name '"
            + token.name()
            + "'.",
        token);
  }
}
