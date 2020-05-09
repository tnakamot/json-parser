package com.github.tnakamot.json.pointer;

/**
 * Thrown when a reference token does not represent an index of a JSON array
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 */
public class InvalidJSONPointerNotIndexException extends InvalidJSONPointerException {
  private final JSONPointerReferenceToken token;

  /**
   * Instantiate the exception with a reference token.
   *
   * @param token a reference token which cannot be intepreted as an index of a JSON array
   */
  public InvalidJSONPointerNotIndexException(JSONPointerReferenceToken token) {
    super(
        "'" + token.text() + "' is not an index of an array.",
        token.pointer().text(),
        token.beginningLocation(),
        token.endLocation());
    this.token = token;
  }

  /**
   * The reference token which cannot be interpreted as an index of a JSON array.
   *
   * @return the reference token which cannot be interpreted as an index of a JSON array
   */
  public JSONPointerReferenceToken token() {
    return token;
  }
}
