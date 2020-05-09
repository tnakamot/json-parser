package com.github.tnakamot.json.pointer;

import com.github.tnakamot.json.value.JSONValue;
import com.github.tnakamot.json.value.JSONValueString;
import com.github.tnakamot.json.value.JSONValueType;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * JSON Pointer implementation compliant with <a href="https://tools.ietf.org/html/rfc6901">RFC
 * 6901</a>.
 *
 * <p>Instances of this class are immutable.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>
 */
public class JSONPointer {
  private final String text;
  private final List<JSONPointerReferenceToken> tokens;

  /**
   * Create a new JSON Pointer instance from a Java String.
   *
   * @param text String which represents a JSON pointer
   * @throws InvalidJSONPointerSyntaxException when the given String cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(String text) throws InvalidJSONPointerSyntaxException {
    this(text, false);
  }

  /**
   * Create a new JSON Pointer instance from a Java String.
   *
   * @param text String which represents a JSON pointer
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @throws InvalidJSONPointerSyntaxException when the given String cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(String text, boolean fragment) throws InvalidJSONPointerSyntaxException {
    this.text = text;

    JSONPointerLexer lexer = new JSONPointerLexer(this);
    this.tokens = lexer.tokenize(fragment);
  }

  /**
   * Create a new JSON Pointer instance from a JSON string value.
   *
   * @param value a JSON string value which represents a JSON pointer
   * @throws InvalidJSONPointerSyntaxException when the given string cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(JSONValueString value) throws InvalidJSONPointerSyntaxException {
    this(value.value());
  }

  /**
   * Create a new JSON Pointer instance from a JSON string value.
   *
   * @param value a JSON string value which represents a JSON pointer
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @throws InvalidJSONPointerSyntaxException when the given string cannot be interpreted as a JSON
   *     Pointer.
   */
  public JSONPointer(JSONValueString value, boolean fragment)
      throws InvalidJSONPointerSyntaxException {
    this(value.value(), fragment);
  }

  /**
   * Return the reference tokens.
   *
   * @return reference tokens
   */
  public JSONPointerReferenceToken[] tokens() {
    return tokens.toArray(new JSONPointerReferenceToken[0]);
  }

  /**
   * Return the pointer string.
   *
   * @return the original pointer string
   */
  public String text() {
    return text;
  }

  /**
   * Evaluate this JSON pointer in the context of the given root value of a JSON document.
   *
   * @param root root value of a JSON document
   * @return
   */
  public JSONValue evaluate(@NotNull JSONValue root) {
    if (root == null) {
      throw new NullPointerException("root cannot be null");
    }

    return null;

    /*
    JSONValue current = root;
    for (int i = 0; i < tokens.length; i++) {
      if (current.type() == JSONValueType.OBJECT) {

      } else if (current.type() == JSONValueType.ARRAY) {

      } else {

      }
    }

    return current;
    */
  }
}
