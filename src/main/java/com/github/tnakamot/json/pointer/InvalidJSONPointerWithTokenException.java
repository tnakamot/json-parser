package com.github.tnakamot.json.pointer;

/** Thrown when evaluation of a JSON Pointer encounters an error. */
public abstract class InvalidJSONPointerWithTokenException extends InvalidJSONPointerException {
  private final JSONPointerReferenceToken token;

  /**
   * Instantiate the exception with a reference token.
   *
   * @param msg error message
   * @param token a reference token which has an error.
   */
  public InvalidJSONPointerWithTokenException(String msg, JSONPointerReferenceToken token) {
    super(msg, token.pointer().text(), token.beginningLocation(), token.endLocation());
    this.token = token;
  }

  /**
   * The reference token which has an error.
   *
   * @return the reference token which has an error.
   */
  public JSONPointerReferenceToken token() {
    return token;
  }
}
