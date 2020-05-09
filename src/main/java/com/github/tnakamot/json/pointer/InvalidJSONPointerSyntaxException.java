package com.github.tnakamot.json.pointer;

/**
 * Thrown when the given pointer String violates syntax defined in <a
 * href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 */
public class InvalidJSONPointerSyntaxException extends InvalidJSONPointerException {
  /**
   * Instantiate the exception with an empty JSON Pointer text.
   *
   * @param msg error messsage to show
   */
  public InvalidJSONPointerSyntaxException(String msg) {
    super(msg, "", 0, 0);
  }

  /**
   * Instantiate the exception with a JSON Pointer text that has a syntax error.
   *
   * @param msg error message to show
   * @param text the JSON Pointer text that has a syntax error
   */
  public InvalidJSONPointerSyntaxException(String msg, String text, int location) {
    super(msg, text, location, location);
  }
}
