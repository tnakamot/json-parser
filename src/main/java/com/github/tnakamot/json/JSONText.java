/*
 *  Copyright (C) 2020 Takashi Nakamoto <nyakamoto@gmail.com>.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.tnakamot.json;

import com.github.tnakamot.json.parser.JSONLexer;
import com.github.tnakamot.json.parser.JSONParser;
import com.github.tnakamot.json.parser.JSONParserErrorMessageFormat;
import com.github.tnakamot.json.parser.JSONParserException;
import com.github.tnakamot.json.token.JSONToken;
import com.github.tnakamot.json.value.JSONValue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one JSON text.
 *
 * <p>Instances of this class are immutable.
 */
public class JSONText {
  private final String text;
  private final Object source;
  private final String name;
  private final String fullName;

  private JSONText(String text, Object source, String name, String fullName) {
    this.text = text;
    this.source = source;
    this.name = name;
    this.fullName = fullName;

    if (!((source instanceof File) || (source instanceof URL) || (source instanceof String))) {
      throw new IllegalArgumentException("source must be File, URL or String");
    }
  }

  /** @return Contents of this JSON text. */
  public String get() {
    return text;
  }

  /**
   * Return the object which represents the source of this JSON text.
   *
   * @return An instance of {@link File}, {@link URL} or {@link String}.
   */
  public Object source() {
    return source;
  }

  /**
   * Tokenize this JSON text.
   *
   * @param errMsgFmt settings of error message format of {@link JSONParserException}
   * @return Sequence of JSON tokens.
   * @throws IOException if an I/O error occurs
   * @throws JSONParserException if there is a syntax error in JSON text
   */
  public List<JSONToken> tokens(JSONParserErrorMessageFormat errMsgFmt)
      throws IOException, JSONParserException {
    List<JSONToken> tokens = new ArrayList<>();
    JSONLexer lexer = new JSONLexer(this, errMsgFmt);
    JSONToken token;

    while ((token = lexer.next()) != null) tokens.add(token);

    return tokens;
  }

  /**
   * Tokenize this JSON text.
   *
   * @return Sequence of JSON tokens.
   * @throws IOException if an I/O error occurs
   * @throws JSONParserException if there is a syntax error in JSON text
   */
  public List<JSONToken> tokens() throws IOException, JSONParserException {
    JSONParserErrorMessageFormat errMsgFmt = JSONParserErrorMessageFormat.builder().build();
    return tokens(errMsgFmt);
  }

  /**
   * Parse this JSON text.
   *
   * @param immutable specify false to create a JSON value tree with modifiable 'object' and
   *     'array'. Otherwise, they will be immutable.
   * @param errMsgFmt settings of error message format of {@link JSONParserException}
   * @return the root JSON value, or null if there is no value.
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  public JSONValue parse(boolean immutable, JSONParserErrorMessageFormat errMsgFmt)
      throws IOException, JSONParserException {
    List<JSONToken> tokens = tokens(errMsgFmt);
    JSONParser parser = new JSONParser(tokens, errMsgFmt);
    return parser.parse(immutable);
  }

  /**
   * Parse this JSON text.
   *
   * <p>The returned object is immutable.
   *
   * @param errMsgFmt settings of error message format of {@link JSONParserException}
   * @return the root JSON value, or null if there is no value.
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  @SuppressWarnings(
      "UnusedReturnValue") // Unit tests only the exceptions. The return values are tested through
  // unit test of the underlying method.
  public JSONValue parse(JSONParserErrorMessageFormat errMsgFmt)
      throws IOException, JSONParserException {
    return parse(true, errMsgFmt);
  }

  /**
   * Parse this JSON text.
   *
   * @param immutable specify false to create a JSON value tree with modifiable 'object' and
   *     'array'. Otherwise, they will be immutable.
   * @return the root JSON value, or null if there is no value.
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  public JSONValue parse(boolean immutable) throws IOException, JSONParserException {
    JSONParserErrorMessageFormat errMsgFmt = JSONParserErrorMessageFormat.builder().build();
    return parse(immutable, errMsgFmt);
  }

  /**
   * Parse this JSON text.
   *
   * <p>The returned object is immutable.
   *
   * @return the root JSON value, or null if there is no value.
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  public JSONValue parse() throws IOException, JSONParserException {
    return parse(true);
  }

  /**
   * Return a string which represents a short name of the source of this JSON text. This is useful
   * to construct an error message of JSON parsers.
   *
   * @return name of this JSON text.
   */
  public String name() {
    return name;
  }

  /**
   * Return a string which represents the full path of the source of this JSON text. This is useful
   * to construct an error message of JSON parsers.
   *
   * @return full path of this JSON text.
   */
  public String fullName() {
    return fullName;
  }

  @Override
  public String toString() {
    return "[JSONText from " + name() + "]";
  }

  /**
   * Reads JSON text from a file and returns an instance of {@link JSONText} for further processing.
   * The file must be encoded using UTF-8.
   *
   * @param file JSON text file.
   * @return An instance of JSON text.
   * @throws IOException in case of an I/O error
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  public static JSONText fromFile(File file) throws IOException {
    if (file == null) {
      throw new NullPointerException("file cannot be null");
    }

    String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);
    return new JSONText(text, file, file.getName(), file.toString());
  }

  /**
   * Reads JSON text from a URL and returns an instance of {@link JSONText} for further processing.
   * The text must be encoded using UTF-8.
   *
   * @param url URL of a JSON text file.
   * @return An instance of JSON text.
   * @throws IOException if an I/O exception occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  public static JSONText fromURL(URL url) throws IOException {
    if (url == null) {
      throw new NullPointerException("url cannot be null");
    }

    /*
     * TODO: To improve the interoperability remove the BOM (U+FEFF) if exists.
     *       The application may do so according to RFC 8259 (it is not mandatory).
     */
    String text = Utils.readURLToString(url, StandardCharsets.UTF_8);

    String[] paths = url.getPath().split("/");
    String name;
    if (paths.length == 0) {
      name = url.toString();
    } else {
      name = paths[paths.length - 1];
    }

    return new JSONText(text, url, name, url.toString());
  }

  /**
   * Convert the given String to an instance of {@link JSONText}.
   *
   * <p>The name of this source is automatically determined.
   *
   * @param str A string which contains JSON text.
   * @return An instance of JSON text.
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  public static JSONText fromString(String str) {
    return fromString(str, "(inner-string)");
  }

  /**
   * Convert the given String to an instance of {@link JSONText}.
   *
   * <p>You can specify the name of this source string. {@link #name()} and {@link #fullName()}
   * return the specified string. The specified will be used, for example, to show an error message
   * from the parse with the name. With a meaningful name, the users of your application will be
   * able to know which JSON text has a syntax error.
   *
   * @param str A string which contains JSON text.
   * @param name A name of this source string.
   * @return An instance of JSON text.
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  public static JSONText fromString(String str, String name) {
    if (str == null) {
      throw new NullPointerException("str cannot be null");
    }

    return new JSONText(str, str, name, name);
  }
}
