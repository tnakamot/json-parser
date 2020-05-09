package com.github.tnakamot.json.pointer;

/**
 * Thrown when a reference token does not represent an index of a JSON array
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 */
public class InvalidJSONPointerNotIndexException extends InvalidJSONPointerWithTokenException {
  public InvalidJSONPointerNotIndexException(JSONPointerReferenceToken token) {
    super("'" + token.text() + "' is not an index of an array.", token);
  }
}
