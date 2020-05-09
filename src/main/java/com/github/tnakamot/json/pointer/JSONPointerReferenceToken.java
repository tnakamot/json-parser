package com.github.tnakamot.json.pointer;

/**
 * Represents one reference token of a JSON Pointer.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>
 */
public class JSONPointerReferenceToken {
  private final JSONPointerReferenceToken previous;
  private final String text;
  private final int begin;
  private final int end;
  private final JSONPointer pointer;

  /**
   * Construct onr reference token of a JSON Pointer.
   *
   * @param previous previous reference token. Null if this is the root.
   * @param text escaped reference token
   * @param begin location of the beginning of this token within the original JSON Pointer string
   * @param end location of the end of this token within the original JSON Pointer string
   * @param pointer JSON Pointer that this reference token belongs to
   */
  JSONPointerReferenceToken(
      JSONPointerReferenceToken previous, String text, int begin, int end, JSONPointer pointer) {
    this.previous = previous;
    this.text = text;
    this.begin = begin;
    this.end = end;
    this.pointer = pointer;

    if (text == null) {
      throw new NullPointerException("text cannot be null");
    } else if (begin < 0) {
      throw new IllegalArgumentException("beign cannot be negative");
    } else if (end < 0) {
      throw new IllegalArgumentException("end cannot be negative");
    } else if (end < begin) {
      throw new IllegalArgumentException("end must be begin or larger");
    } else if (pointer == null) {
      throw new NullPointerException("pointer cannot be null");
    }
  }

  /**
   * Previous reference token which represents a parent node.
   *
   * @return Previous reference token which represents a parent node. Null if this is the root
   *     value.
   */
  public JSONPointerReferenceToken previous() {
    return previous;
  }

  /**
   * String representation of the JSON Pointer of the parent. If this is the parent is the root
   * value, this method returns an empty string.
   *
   * @return string representation of the JSON Pointer of the parent.
   */
  public String parent() {
    JSONPointerReferenceToken token = previous;
    StringBuilder sb = new StringBuilder();
    while (token != null) {
      sb.insert(0, token.text);
      sb.insert(0, "/");
      token = token.previous();
    }

    return sb.toString();
  }

  /**
   * Unescaped reference token as a member name of a JSON object value.
   *
   * @return Unescaped reference token.
   */
  public String name() {
    return text.replace("~1", "/").replace("~0", "~");
  }

  /**
   * Reference token as an index of a JSON array value.
   *
   * @return index of a JSON array value
   */
  public int index() throws InvalidJSONPointerNotIndexException {
    if (!text.matches("0|([1-9][0-9]*)")) {
      throw new InvalidJSONPointerNotIndexException(this);
    }

    return Integer.parseInt(text);
  }

  /**
   * escaped reference token.
   *
   * @return escaped reference token.
   */
  public String text() {
    return text;
  }

  /**
   * Location of the beginning of the token within the original JSON Pointer string.
   *
   * @return location of the beginning of the token within the original JSON Pointer string.
   */
  public int beginningLocation() {
    return begin;
  }

  /**
   * Location of the end of the token within the original JSON Pointer string.
   *
   * @return location of the end of the token within the original JSON Pointer string.
   */
  public int endLocation() {
    return end;
  }

  /**
   * JSON Pointer that this reference token belongs to.
   *
   * @return JSON Pointer that this token belongs to.
   */
  public JSONPointer pointer() {
    return pointer;
  }
}
