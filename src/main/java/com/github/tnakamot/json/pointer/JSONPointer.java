package com.github.tnakamot.json.pointer;

import com.github.tnakamot.json.value.JSONValueString;
import java.util.Arrays;

/**
 * JSON Pointer implementation compliant with <a href="https://tools.ietf.org/html/rfc6901">RFC
 * 6901</a>.
 *
 * <p>Instances of this class are immutable.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>
 */
public class JSONPointer {
  private final String[] tokens;

  /**
   * Create a new JSON Pointer instance from a Java String.
   *
   * @param pointer String which represents a JSON pointer
   * @throws InvalidJSONPointerSyntaxException when the given String cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(String pointer) throws InvalidJSONPointerSyntaxException {
    this(pointer, false);
  }

  /**
   * Create a new JSON Pointer instance from a Java String.
   *
   * @param pointer String which represents a JSON pointer
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @throws InvalidJSONPointerSyntaxException when the given String cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(String pointer, boolean fragment) throws InvalidJSONPointerSyntaxException {
    this.tokens = parse(pointer, fragment);
  }

  /**
   * Create a new JSON Pointer instance from a JSON string value.
   *
   * @param pointer String which represents a JSON pointer
   * @throws InvalidJSONPointerSyntaxException when the given string cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(JSONValueString pointer) throws InvalidJSONPointerSyntaxException {
    this(pointer.value());
  }

  /**
   * Return the unescaped reference tokens.
   *
   * @return unescaped reference tokens.
   */
  public String[] tokens() {
    return Arrays.copyOf(tokens, tokens.length);
  }

  /**
   * Create a new JSON Pointer instance from a JSON string value.
   *
   * @param pointer a JSON string value which represents a JSON pointer
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @throws InvalidJSONPointerSyntaxException when the given string cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(JSONValueString pointer, boolean fragment)
      throws InvalidJSONPointerSyntaxException {
    this(pointer.value(), fragment);
  }

  private static String[] parse(String pointer, boolean fragment)
      throws InvalidJSONPointerSyntaxException {
    String str;

    if (fragment) {
      if (pointer.equals("#")) {
        return new String[0];
      } else if (pointer.startsWith("#/")) {
        str = pointer.substring(2);
      } else {
        throw new InvalidJSONPointerSyntaxException(
            "JSON Pointer (as URI fragment identifier) must be '#' or start with '#/'", pointer);
      }
    } else {
      if (pointer.isEmpty()) {
        return new String[0];
      } else if (pointer.startsWith("/")) {
        str = pointer.substring(1);
      } else {
        throw new InvalidJSONPointerSyntaxException(
            "JSON Pointer must be an empty string or start with '/'", pointer);
      }
    }

    if (pointer.endsWith("/")) {
      throw new InvalidJSONPointerSyntaxException("JSON Pointer must not end with '/'", pointer);
    }

    String[] tokens = str.split("/");
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].isEmpty()) {
        throw new InvalidJSONPointerSyntaxException(
            "JSON Pointer must not have '/' in row", pointer);
      }

      tokens[i] = tokens[i].replace("~1", "/");
      tokens[i] = tokens[i].replace("~0", "~");
    }

    return tokens;
  }
}
