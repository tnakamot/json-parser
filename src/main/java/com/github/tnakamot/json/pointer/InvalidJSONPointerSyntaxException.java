package com.github.tnakamot.json.pointer;

/**
 * Thrown when the given pointer String violates syntax defined in <a
 * href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 */
public class InvalidJSONPointerSyntaxException extends Exception {
  private final String pointerString;

  public InvalidJSONPointerSyntaxException(String msg, String pointerString) {
    super(msg + ": " + pointerString);

    this.pointerString = pointerString;
  }

  /**
   * Returns the invalid pointer string.
   *
   * @return the invalid pointer string.
   */
  public String getPointerString() {
    return pointerString;
  }
}
