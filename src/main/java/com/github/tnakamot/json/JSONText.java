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
import com.github.tnakamot.json.parser.JSONParserErrorHandlingOptions;
import com.github.tnakamot.json.parser.JSONParserException;
import com.github.tnakamot.json.parser.JSONParserResult;
import com.github.tnakamot.json.pointer.InvalidJSONPointerException;
import com.github.tnakamot.json.pointer.JSONPointer;
import com.github.tnakamot.json.token.JSONToken;
import com.github.tnakamot.json.value.JSONValue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one JSON text.
 *
 * <p>Instances of this class is not immutable, but thread-safe.
 */
public class JSONText {
  private final String text;
  private final Object source;
  private final URI sourceURI;
  private final String name;
  private JSONParserResult parserResult;

  private JSONText(
      @NotNull String text, @NotNull Object source, @NotNull URI sourceURI, @NotNull String name) {
    this.text = text;
    this.source = source;
    this.sourceURI = sourceURI;
    this.name = name;

    this.parserResult = null;

    if (!((source instanceof File) || (source instanceof URL) || (source instanceof String))) {
      throw new IllegalArgumentException("source must be File, URL or String");
    }
  }

  /**
   * Returns the content of this JSON text as a string.
   *
   * @return Contents of this JSON text.
   */
  @NotNull
  public String get() {
    return text;
  }

  /**
   * Return the object which represents the source of this JSON text.
   *
   * @return An instance of {@link File}, {@link URL} or {@link String}.
   */
  @NotNull
  public Object source() {
    return source;
  }

  /**
   * Return a short name of the source of this JSON text. This is useful to construct an error
   * message of JSON parsers.
   *
   * @return name of this JSON text.
   */
  @NotNull
  public String name() {
    return name;
  }

  /** Return a URI of the source of this JSON text. */
  @NotNull
  public URI uri() {
    return sourceURI;
  }

  /**
   * Tokenize this JSON text.
   *
   * @param options parser options
   * @return Sequence of JSON tokens.
   * @throws IOException if an I/O error occurs
   * @throws JSONParserException if there is a syntax error in JSON text
   */
  @NotNull
  public synchronized List<JSONToken> tokens(@NotNull JSONParserErrorHandlingOptions options)
      throws IOException, JSONParserException {
    List<JSONToken> tokens = new ArrayList<>();
    JSONLexer lexer = new JSONLexer(this, options);
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
  @NotNull
  public synchronized List<JSONToken> tokens() throws IOException, JSONParserException {
    JSONParserErrorHandlingOptions options = JSONParserErrorHandlingOptions.builder().build();
    return tokens(options);
  }

  /**
   * Parse this JSON text.
   *
   * <p>The returned instance is immutable.
   *
   * @param options parser options
   * @return parse result
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  @NotNull
  public synchronized JSONParserResult parse(@NotNull JSONParserErrorHandlingOptions options)
      throws IOException, JSONParserException {
    if (parserResult == null) {
      List<JSONToken> tokens = tokens(options);
      JSONParser parser = new JSONParser(tokens, options);
      parserResult = parser.parse();
    }

    return parserResult;
  }

  /**
   * Parse this JSON text.
   *
   * <p>The returned instance is immutable.
   *
   * @return parse result
   * @throws JSONParserException if there is a syntax error in the JSON text
   * @throws IOException if an I/O error occurs
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  @NotNull
  public synchronized JSONParserResult parse() throws IOException, JSONParserException {
    JSONParserErrorHandlingOptions options = JSONParserErrorHandlingOptions.builder().build();
    return parse(options);
  }

  /**
   * Returns whether this JSON text has been successfully parsed in the past.
   *
   * <p>If this method returns false, {@link #evaluate(String)} and its variants result in {@link
   * IllegalStateException};
   *
   * @return whether this JSON text has been successfully parsed in the past.
   */
  public synchronized boolean isParsed() {
    return parserResult != null;
  }

  /**
   * Evaluate the given JSON Pointer and return the found JSON value.
   *
   * <p>{@link #parse()} (or its variant) must be successfully called before this method.
   *
   * <p>Note that this method results in {@link InvalidJSONPointerException} if this JSON text does
   * not have any JSON value.
   *
   * @param pointer a string representation of a JSON Pointer
   * @return the JSON value of the pointer evaluation result
   * @throws InvalidJSONPointerException when the JSON Pointer has an error
   * @throws IllegalStateException if this JSON text has not been parsed yet
   */
  @NotNull
  public synchronized JSONValue evaluate(@NotNull String pointer)
      throws InvalidJSONPointerException {
    // TODO: judge the content type and set 'fragment' argument accordingly
    return evaluate(pointer, false);
  }

  /**
   * Evaluate the given JSON Pointer and return the found JSON value.
   *
   * <p>{@link #parse()} (or its variant) must be successfully called before this method.
   *
   * <p>Note that this method results in {@link InvalidJSONPointerException} if this JSON text does
   * not have any JSON value.
   *
   * @param pointer a string representation of a JSON Pointer
   * @param fragment specify true to handle the given string as a URI fragment identifier starting
   *     with '#".
   * @return the JSON value of the pointer evaluation result
   * @throws InvalidJSONPointerException when the JSON Pointer has an error
   * @throws IllegalStateException if this JSON text has not been parsed yet
   */
  @NotNull
  public synchronized JSONValue evaluate(@NotNull String pointer, boolean fragment)
      throws InvalidJSONPointerException {
    return evaluate(new JSONPointer(pointer, fragment));
  }

  /**
   * Evaluate the given JSON Pointer and return the found JSON value.
   *
   * <p>{@link #parse()} (or its variant) must be successfully called before this method.
   *
   * <p>Note that this method results in {@link InvalidJSONPointerException} if this JSON text does
   * not have any JSON value.
   *
   * @param pointer JSON pointer
   * @return the JSON value of the pointer evaluation result
   * @throws InvalidJSONPointerException when the JSON Pointer has an error
   * @throws IllegalStateException if this JSON text has not been parsed yet
   */
  @NotNull
  public synchronized JSONValue evaluate(@NotNull JSONPointer pointer)
      throws InvalidJSONPointerException {
    if (parserResult == null) {
      throw new IllegalStateException(
          "JSON text needs to be parsed first before evaluating a JSON Pointer");
    }

    JSONValue root = parserResult.root();
    if (root == null) {
      throw new IllegalStateException("This JSON text has no JSON value");
    }

    return pointer.evaluate(root);
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
  @NotNull
  public static JSONText fromFile(@NotNull File file) throws IOException {
    String text = Files.readString(file.toPath(), StandardCharsets.UTF_8);

    return new JSONText(text, file, file.toURI(), file.getName());
  }

  /**
   * Reads JSON text from a URL and returns an instance of {@link JSONText} for further processing.
   * The text must be encoded using UTF-8.
   *
   * @param url URL of a JSON text file.
   * @return An instance of JSON text.
   * @throws IOException if an I/O exception occurs
   * @throws URISyntaxException if this URL is not formatted strictly according to to RFC2396 and
   *     cannot be converted to a URI.
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  @NotNull
  public static JSONText fromURL(@NotNull URL url) throws IOException, URISyntaxException {
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

    return new JSONText(text, url, url.toURI(), name);
  }

  /**
   * Convert the given String to an instance of {@link JSONText}.
   *
   * <p>The name of this source is automatically determined. See {@link #fromString(String, String)}
   * for more details.
   *
   * @param str A string which contains JSON text.
   * @return An instance of JSON text.
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   * @see #fromString(String, String)
   */
  @NotNull
  public static JSONText fromString(@NotNull String str) {
    return fromString(str, null);
  }

  /**
   * Convert the given String to an instance of {@link JSONText}.
   *
   * <p>Specify the name, then {@link #name()} returns the specified string. The specified name will
   * be used, for example, for the parser to show an error message . With a meaningful name, the
   * users of your application will be able to know which JSON text has a syntax error.
   *
   * <p>If the name is not specified (= null is given), the name is automatically determined by this
   * library, but the users may not understand where does that the JSON text come from, so it is not
   * recommended.
   *
   * @param str A string which contains JSON text.
   * @param name A name of this source string.
   * @return An instance of JSON text.
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character
   *     Encoding</a>
   */
  @NotNull
  public static JSONText fromString(@NotNull String str, @Nullable String name) {
    URI sha1URI = sha1URI(str);

    if (name == null) {
      return new JSONText(str, str, sha1URI, sha1URI.toString());
    } else {
      return new JSONText(str, str, sha1URI, name);
    }
  }

  private static String sha1(@NotNull String str) {
    return org.apache.commons.codec.digest.DigestUtils.sha1Hex(str);
  }

  @NotNull
  private static URI sha1URI(@NotNull String str) {
    try {
      return new URI("urn:sha1:" + sha1(str));
    } catch (URISyntaxException ex) {
      throw new RuntimeException("the code must not reach here", ex);
    }
  }
}
