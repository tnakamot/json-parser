package com.github.tnakamot.json.pointer;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of lexical analyzer for JSON Pointer. This implementation complies with <a
 * href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>.
 *
 * <p>Instances of this class are disposal. A new instance must be created to tokenize one JSON
 * Pointer.
 *
 * <p>Instances of this class are not thread-safe.
 *
 * @see <a href="https://tools.ietf.org/html/rfc6901">RFC 6901</a>
 */
public class JSONPointerLexer {
  private final JSONPointer pointer;
  private final PushbackReader reader;
  int location;
  boolean eof;

  /**
   * Create an instance of a lexical analyzer of the given JSON Pointer
   *
   * @param pointer JSON Pointer to tokenize
   */
  JSONPointerLexer(JSONPointer pointer) {
    if (pointer == null) {
      throw new NullPointerException("pointer cannot be null");
    }

    this.pointer = pointer;
    this.reader = new PushbackReader(new StringReader(pointer.text()));
    this.location = 0;
  }

  private char read() {
    try {
      location += 1;
      int ich = reader.read();
      if (ich == -1) {
        eof = true;
      }
      return (char) ich;
    } catch (IOException ex) {
      throw new RuntimeException("never reach here", ex);
    }
  }

  private void readAsFragment() throws InvalidJSONPointerSyntaxException {
    String errMsg;
    char ch = read();

    if (eof) {
      errMsg = "JSON Pointer (as URI fragment identifier) cannot be empty. Must start with '#'.";
      throw new InvalidJSONPointerSyntaxException(errMsg);
    } else if (ch != '#') {
      errMsg = "JSON Pointer (as URI fragment identifier) must start with '#'.";
      throw new InvalidJSONPointerSyntaxException(errMsg, pointer.text(), 0);
    }
  }

  private JSONPointerReferenceToken next(JSONPointerReferenceToken current)
      throws InvalidJSONPointerSyntaxException {
    StringBuilder tokenSB = new StringBuilder();
    int begin = location;
    boolean escaped = false;
    String errMsg;

    while (true) {
      char ch = read();
      if (escaped) {
        if (ch == '0' || ch == '1') {
          tokenSB.append(ch);
          escaped = false;
        } else if (eof) {
          errMsg = "Reached the end of the JSON Pointer text after '~'. It must be '0' or '1'.";
          throw new InvalidJSONPointerSyntaxException(errMsg, pointer.text(), location - 1);
        } else {
          errMsg = "Unexpected character '" + ch + "' after '~'. It must be '0' or '1'.";
          throw new InvalidJSONPointerSyntaxException(errMsg, pointer.text(), location - 1);
        }
      } else {
        if (eof) {
          return new JSONPointerReferenceToken(
              current, tokenSB.toString(), begin, location - 1, pointer);
        } else if (ch == '~') {
          escaped = true;
          tokenSB.append('~');
        } else if (ch == '/') {
          return new JSONPointerReferenceToken(
              current, tokenSB.toString(), begin, location - 1, pointer);
        } else {
          tokenSB.append(ch);
        }
      }
    }
  }

  /**
   * Tokenize the given JSON Pointer.
   *
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @throws InvalidJSONPointerSyntaxException when the given String cannot be interpreted as a JSON
   *     Pointer.
   */
  public List<JSONPointerReferenceToken> tokenize(boolean fragment)
      throws InvalidJSONPointerSyntaxException {

    List<JSONPointerReferenceToken> tokens = new LinkedList<>();
    JSONPointerReferenceToken current = null;

    if (fragment) {
      readAsFragment();
    }

    // Check if it points to the root value.
    char ch = read();
    if (eof) {
      return tokens;
    } else if (ch != '/') {
      String errMsg = "JSON Pointer must start with '/'.";
      throw new InvalidJSONPointerSyntaxException(errMsg, pointer.text(), location - 1);
    }

    while (!eof) {
      current = next(current);
      tokens.add(current);
    }

    return tokens;
  }
}
