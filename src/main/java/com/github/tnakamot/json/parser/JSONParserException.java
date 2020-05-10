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
import com.github.tnakamot.json.token.StringLocation;
import org.jetbrains.annotations.NotNull;

/**
 * Thrown when a JSON lexical analyzer fails to tokenize a given JSON text due to a syntax error.
 *
 * <p>Instances of this class are immutable.
 *
 * <p>The error message can be customized by {@link JSONParserErrorHandlingOptions}. The message
 * format is basically
 *
 * <pre>
 * (name):(position): (message)
 * </pre>
 *
 * <p>(name) is the short name of the source JSON text, which is typically a file name. If {@link
 * JSONParserErrorHandlingOptions#showURI()} is true, the URI is shown instead.
 *
 * <p>(position) shows the location where the error is detected within the source JSON text. If
 * {@link JSONParserErrorHandlingOptions#showLineAndColumnNumber()} is false, the position in number
 * of Unicode characters is shown (starting from zero). If it is true, the line number and the
 * column (both starting from one) separated by ":" is shown.
 *
 * <p>If {@link JSONParserErrorHandlingOptions#showErrorLine()} is true, a line which contains the
 * error is shown additionally with a position marker.
 *
 * <p>Here is an example of a JSON text which has an error:
 *
 * <pre>
 * {
 *   "key": My name is JSON
 * }
 * </pre>
 *
 * <p>If
 *
 * <ul>
 *   <li>{@link JSONParserErrorHandlingOptions#showURI()} is false
 *   <li>{@link JSONParserErrorHandlingOptions#showLineAndColumnNumber()} is true
 *   <li>{@link JSONParserErrorHandlingOptions#showErrorLine()} is false
 * </ul>
 *
 * <p>the error message from the parser will be
 *
 * <pre>
 * hello.json:12: unknown token starting with 'M'
 * </pre>
 *
 * <p>If
 *
 * <ul>
 *   <li>{@link JSONParserErrorHandlingOptions#showURI()} is true
 *   <li>{@link JSONParserErrorHandlingOptions#showLineAndColumnNumber()} is false
 *   <li>{@link JSONParserErrorHandlingOptions#showErrorLine()} is true
 * </ul>
 *
 * <p>the error message from the parser will be
 *
 * <pre>
 * /path/to/hello.json:12: unknown token starting with 'M'
 * "key": My name is JSON
 *        ^
 * </pre>
 */
public class JSONParserException extends Exception {
  private final String msg;
  private final JSONText source;
  private final StringLocation begin;
  private final StringLocation end;
  private final JSONParserErrorHandlingOptions errMsgFmt;

  /**
   * Instantiate this exception.
   *
   * @param source JSON text that has a problem
   * @param location location of the problem within the source JSON text
   * @param errMsgFmt configuration to change the error message format of this exception
   * @param msg error message which explains the problem
   */
  public JSONParserException(
      @NotNull JSONText source,
      @NotNull StringLocation location,
      @NotNull JSONParserErrorHandlingOptions errMsgFmt,
      @NotNull String msg) {
    super(msg);
    this.msg = msg;
    this.source = source;
    this.begin = location;
    this.end = location;
    this.errMsgFmt = errMsgFmt;
  }

  /**
   * Instantiate this exception.
   *
   * @param source JSON text that has a problem
   * @param begin beginning location of the problem within the source JSON text
   * @param end end location of the problem within the source JSON text
   * @param errMsgFmt configuration to change the error message format of this exception
   * @param msg error message which explains the problem
   */
  public JSONParserException(
      @NotNull JSONText source,
      @NotNull StringLocation begin,
      @NotNull StringLocation end,
      @NotNull JSONParserErrorHandlingOptions errMsgFmt,
      @NotNull String msg) {
    super(msg);
    this.msg = msg;
    this.source = source;
    this.begin = begin;
    this.end = end;
    this.errMsgFmt = errMsgFmt;
  }

  /** @return the JSON text that cannot be tokenized properly. */
  public JSONText source() {
    return source;
  }

  /**
   * Returns the beginning location of hte problem where the lexical analyzer failed to tokenize the
   * given JSON text.
   *
   * @return beginning location of the problem within the source JSON text
   */
  public StringLocation begin() {
    return begin;
  }

  /**
   * Returns the end location of hte problem where the lexical analyzer failed to tokenize the given
   * JSON text.
   *
   * @return end location of the problem within the source JSON text
   */
  public StringLocation end() {
    return begin;
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder();
    if (errMsgFmt.showURI()) {
      sb.append(source.uri().toString());
    } else {
      sb.append(source.name());
    }

    sb.append(":");

    if (errMsgFmt.showLineAndColumnNumber()) {
      sb.append(begin.line());
      sb.append(":");
      sb.append(begin.column());
    } else {
      sb.append(begin.position());
    }

    sb.append(": ");
    sb.append(msg);

    if (errMsgFmt.showErrorLine()) {
      // TODO: consider multiple lines
      // TODO: show range

      String[] lines = source.get().split("\r|(\r?\n)");
      String line = lines[begin.line() - 1];
      sb.append(System.lineSeparator());
      sb.append(line);
      sb.append(System.lineSeparator());
      sb.append(" ".repeat(begin.column() - 1));
      sb.append("^");
    }

    return sb.toString();
  }
}
