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

package com.github.tnakamot.json.parser;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.token.*;
import com.github.tnakamot.json.value.*;

import java.io.PrintStream;
import java.util.*;
import org.jetbrains.annotations.NotNull;

import static com.github.tnakamot.json.token.JSONToken.*;

/**
 * An implementation of a parser of JSON texts. This implementation complies with <a
 * href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>.
 *
 * <p>Instances of this class are disposal. A new instance must be created to parse one sequence of
 * JSON tokens.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
public final class JSONParser {
  private final List<JSONToken> tokens;
  private final JSONParserErrorHandlingOptions options;
  private int position;
  private boolean parsed;

  private final List<List<JSONValueString>> duplicateKeys;
  private final List<JSONValueNumber> numbersTooBigForDouble;

  private static final String stringToken = "A string";
  private static final String valueToken =
      "A JSON value (object, array, number, string, boolean or null)";
  private static final String valueOrEndArrayToken =
      String.format("%s or '%s'", valueToken, JSON_END_ARRAY);
  private static final String valueSepOrEndArrayToken =
      String.format("'%s' or '%s'", JSON_VALUE_SEPARATOR, JSON_END_ARRAY);
  private static final String stringOrEndObjectToken =
      String.format("%s or '%s'", stringToken, JSON_END_OBJECT);
  private static final String valueSepOrEndObjectToken =
      String.format("'%s' or '%s'", JSON_VALUE_SEPARATOR, JSON_END_OBJECT);
  private static final String nameSepToken = String.format("'%s'", JSON_NAME_SEPARATOR);

  /**
   * Create an instance of JSON parse for the given sequence of JSON tokens.
   *
   * @param tokens a sequence of JSON tokens to parse
   * @param options settings of error message format of {@link JSONParserException}
   */
  public JSONParser(List<JSONToken> tokens, JSONParserErrorHandlingOptions options) {
    if (tokens == null) {
      throw new NullPointerException("tokens cannot be null");
    } else if (options == null) {
      throw new NullPointerException("errMsgConfig cannot be null");
    }

    this.tokens = new ArrayList<>(tokens);
    this.options = options;
    this.position = 0;
    this.duplicateKeys = new LinkedList<>();
    this.numbersTooBigForDouble = new LinkedList<>();
    this.parsed = false;
  }

  /**
   * Parse the given sequence of JSON tokens and return the root JSON value.
   *
   * <p>The returned object is immutable.
   *
   * @return parser result
   * @throws JSONParserException if there is a semantic error in the sequence of JSON tokens
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  @NotNull
  public JSONParserResult parse() throws JSONParserException {
    return parse(true);
  }

  /**
   * Parse the given sequence of JSON tokens and return the root JSON value.
   *
   * @param immutable Specify false to create a JSON value tree with modifiable 'object' and
   *     'array'.
   * @return parse result
   * @throws JSONParserException if there is a semantic error in the sequence of JSON tokens
   * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
   */
  @NotNull
  public JSONParserResult parse(boolean immutable) throws JSONParserException {
    if (parsed) {
      throw new IllegalStateException("can parse only once");
    }

    if (tokens.size() == 0) {
      return new JSONParserResult(null, duplicateKeys, numbersTooBigForDouble);
    }

    JSONValue value = readValue(immutable);
    if (position < tokens.size()) {
      unexpectedToken(popToken(), "EOF");
    }

    showWarning();
    parsed = true;
    return new JSONParserResult(value, duplicateKeys, numbersTooBigForDouble);
  }

  private void showWarning() {
    PrintStream out = options.warningStream();
    if (out != null) {
      out.print(warningOfDuplicateKeys());
      out.print(warningOfTooBigNumbersForDouble());
    }
  }

  private String warningLineAndLocation(String[] lines, StringRange range) {
    // The code below assumes the line number of the beginning of the
    // JSON string token is the same as that of the end. This is true,
    // because RFC 8259 does not allow you to include control characters
    // including CR and LF in a number or a string.

    StringBuilder sb = new StringBuilder();
    StringLocation begin = range.beginning();
    StringLocation end = range.end();

    sb.append("  At line ")
        .append(begin.line())
        .append(", column ")
        .append(begin.column())
        .append(" - ")
        .append(end.column())
        .append(System.lineSeparator());

    sb.append("    ").append(lines[begin.line() - 1]).append(System.lineSeparator());

    sb.append("    ").append(" ".repeat(begin.column() - 1));
    sb.append("^".repeat(end.column() - begin.column() + 1));
    sb.append(System.lineSeparator());

    return sb.toString();
  }

  private String warningOfTooBigNumbersForDouble() {
    StringBuilder sb = new StringBuilder();
    JSONText source = tokens.get(0).source();
    String[] lines = source.get().split("\r|(\r?\n)");

    for (JSONValueNumber num : numbersTooBigForDouble) {
      sb.append(warningHeader());
      sb.append(num.text()).append("' is too big to handle with Java 'double' primitive");

      JSONToken token = num.token();
      if (token != null) {
        sb.append(warningLineAndLocation(lines, token.range()));
      }
    }

    return sb.toString();
  }

  private String warningOfDuplicateKeys() {
    StringBuilder sb = new StringBuilder();
    JSONText source = tokens.get(0).source();
    String[] lines = source.get().split("\r|(\r?\n)");

    for (List<JSONValueString> dup : duplicateKeys) {
      sb.append(warningHeader());
      sb.append("duplicate key '").append(dup.get(0).value()).append("': ");
      sb.append(System.lineSeparator());

      for (JSONValueString key : dup) {
        JSONToken token = key.token();
        if (token != null) {
          sb.append(warningLineAndLocation(lines, token.range()));
        }
      }
    }

    return sb.toString();
  }

  private String warningHeader() {
    JSONText source = tokens.get(0).source();

    StringBuilder sb = new StringBuilder();
    if (options.showURI()) {
      sb.append(source.uri().toString());
    } else {
      sb.append(source.name());
    }
    sb.append(": ");

    return sb.toString();
  }

  private JSONToken popToken() {
    JSONToken token = tokens.get(position);
    position += 1;
    return token;
  }

  private JSONToken lastToken() {
    return tokens.get(tokens.size() - 1);
  }

  private void pushBack() {
    position -= 1;
  }

  private void unexpectedEof(String expectedToken) throws JSONParserException {
    String msg = String.format("Reached EOF unexpectedly. %s was expected.", expectedToken);
    JSONToken lastToken = lastToken();

    throw new JSONParserException(lastToken.source(), lastToken.endLocation(), options, msg);
  }

  private void unexpectedToken(JSONToken token, String expectedToken) throws JSONParserException {
    String msg =
        String.format("Unexpected token '%s'. %s was expected.", token.text(), expectedToken);
    throw new JSONParserException(
        token.source(), token.beginningLocation(), token.endLocation(), options, msg);
  }

  private JSONValue readValue(boolean immutable) throws JSONParserException {
    try {
      JSONToken token = popToken();

      switch (token.type()) {
        case BEGIN_ARRAY:
          return readArray(immutable);
        case BEGIN_OBJECT:
          return readObject(immutable);
        case NULL:
          return new JSONValueNull(token);
        case BOOLEAN:
          return new JSONValueBoolean((JSONTokenBoolean) token);
        case NUMBER:
          JSONValueNumber number = new JSONValueNumber((JSONTokenNumber) token);
          if (Double.isInfinite(number.toDouble())) {
            if (options.failOnTooBigNumberForDouble()) {
              String msg =
                  "'" + token.text() + "' is too big to handle with Java 'double' primitive";
              throw new JSONParserException(token.source(), token.range(), options, msg);
            } else {
              numbersTooBigForDouble.add(number);
              return new JSONValueNumber((JSONTokenNumber) token);
            }
          } else {
            return new JSONValueNumber((JSONTokenNumber) token);
          }
        case STRING:
          return new JSONValueString((JSONTokenString) token);
        default:
          unexpectedToken(token, valueToken);
      }
    } catch (IndexOutOfBoundsException ex) {
      unexpectedEof(valueToken);
    }

    throw new RuntimeException("never reach here");
  }

  private JSONValueArray readArray(boolean immutable) throws JSONParserException {
    JSONValueArrayMutable array = new JSONValueArrayMutable();

    // read the first value (or it can be an empty array)
    try {
      JSONToken token = popToken();

      switch (token.type()) {
        case END_ARRAY:
          // an empty array
          if (immutable) {
            return array.toImmutable();
          } else {
            return array;
          }
        case NULL:
        case BOOLEAN:
        case NUMBER:
        case STRING:
        case BEGIN_ARRAY:
        case BEGIN_OBJECT:
          pushBack();
          array.add(readValue(immutable));
          break;
        default:
          unexpectedToken(token, valueOrEndArrayToken);
      }
    } catch (IndexOutOfBoundsException ex) {
      unexpectedEof(valueOrEndArrayToken);
    }

    while (true) {
      // read a next value or an end array
      try {
        JSONToken token = popToken();

        switch (token.type()) {
          case END_ARRAY:
            if (immutable) {
              return array.toImmutable();
            } else {
              return array;
            }
          case VALUE_SEPARATOR:
            array.add(readValue(immutable));
            break;
          default:
            unexpectedToken(token, valueSepOrEndArrayToken);
        }
      } catch (IndexOutOfBoundsException ex) {
        unexpectedEof(valueSepOrEndArrayToken);
      }
    }
  }

  private Map.Entry<JSONValueString, JSONValue> readMember(boolean immutable)
      throws JSONParserException {
    JSONValueString key;

    // read a key
    try {
      JSONToken token = popToken();

      if (token.type() == JSONTokenType.STRING) {
        key = new JSONValueString((JSONTokenString) token);
      } else {
        unexpectedToken(token, stringToken);
        key = null;
      }
    } catch (IndexOutOfBoundsException ex) {
      unexpectedEof(stringToken);
      key = null;
    }

    // read a name separator
    try {
      JSONToken token = popToken();

      if (token.type() != JSONTokenType.NAME_SEPARATOR) {
        unexpectedToken(token, nameSepToken);
      }
    } catch (IndexOutOfBoundsException ex) {
      unexpectedEof(nameSepToken);
    }

    JSONValue value = readValue(immutable);
    JSONValueString rKey = key;

    return new Map.Entry<>() {
      @Override
      public JSONValueString getKey() {
        return rKey;
      }

      @Override
      public JSONValue getValue() {
        return value;
      }

      @Override
      public JSONValue setValue(JSONValue jsonValue) {
        throw new UnsupportedOperationException();
      }
    };
  }

  private JSONValueObject readObject(boolean immutable) throws JSONParserException {
    JSONValueObjectMutable object = new JSONValueObjectMutable();
    Map<String, List<JSONValueString>> duplicates = new HashMap<>();

    // read the first member (or it can be an empty object)
    try {
      JSONToken token = popToken();

      switch (token.type()) {
        case END_OBJECT:
          // empty object
          if (immutable) {
            return object.toImmutable();
          } else {
            return object;
          }
        case STRING:
          pushBack();
          Map.Entry<JSONValueString, JSONValue> member = readMember(immutable);
          object.put(member.getKey(), member.getValue());

          LinkedList<JSONValueString> dup = new LinkedList<>();
          dup.add(member.getKey());
          duplicates.put(member.getKey().value(), dup);
          break;
        default:
          unexpectedToken(token, stringOrEndObjectToken);
      }
    } catch (IndexOutOfBoundsException ex) {
      unexpectedEof(stringOrEndObjectToken);
    }

    while (true) {
      // read a next member or an end object
      try {
        JSONToken token = popToken();

        switch (token.type()) {
          case END_OBJECT:
            for (List<JSONValueString> dup : duplicates.values()) {
              if (dup.size() > 1) {
                duplicateKeys.add(dup);
              }
            }

            if (immutable) {
              return object.toImmutable();
            } else {
              return object;
            }
          case VALUE_SEPARATOR:
            Map.Entry<JSONValueString, JSONValue> member = readMember(immutable);

            if (object.containsKey(member.getKey())) {
              if (options.failOnDuplicateKey()) {
                String keyStr = member.getKey().value();
                String msg = "Found duplicate key '" + keyStr + "' in the same JSON object.";
                //noinspection ConstantConditions
                throw new JSONParserException(
                    token.source(), member.getKey().token().range(), options, msg);
              } else {
                duplicates.get(member.getKey().value()).add(member.getKey());
                object.put(member.getKey(), member.getValue());
              }
            } else {
              object.put(member.getKey(), member.getValue());

              LinkedList<JSONValueString> dup = new LinkedList<>();
              dup.add(member.getKey());
              duplicates.put(member.getKey().value(), dup);
            }
            break;
          default:
            unexpectedToken(token, valueSepOrEndObjectToken);
        }
      } catch (IndexOutOfBoundsException ex) {
        unexpectedEof(valueSepOrEndObjectToken);
      }
    }
  }
}
