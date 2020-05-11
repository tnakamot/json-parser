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

import java.io.PrintStream;
import org.jetbrains.annotations.Nullable;

/**
 * A data structure to hold options to change the behavior of the JSON parser when it encounters a
 * questionable JSON token and switches to change the error message format {@link
 * JSONParserException}. See {@link JSONParserException} for more details about how the error
 * messages are formatted.
 *
 * <p>Instances of this class are immutable.
 *
 * <p>This class follows Builder Pattern. To make an instance of this class, use the code snippet
 * something like below:
 *
 * <pre>
 * JSONLexerErrorErrorHandlingOptions opts =
 *     JSONLexerErrorMessageConfiguration.builder()
 *         .showURI(false)
 *         .showLineAndColumnNumber(true)
 *         .showErrorLine(false)
 *         .failOnDuplicateKey(false)
 *         .failOnTooBigNumber(false)
 *         .warningStream(System.err)
 *         .build();
 * </pre>
 *
 * <p>The invocation of methods between {@link #builder()} and {@link Builder#build()} are optional.
 * If you want to use the default configuration, just do not call them.
 *
 * <p>The default value may change in the future, so do not rely on them if you really need a
 * specific error handling behavior.
 */
public class JSONParserErrorHandlingOptions {
  private final boolean showURI;
  private final boolean showLineAndColumnNumber;
  private final boolean showErrorLine;
  private final boolean failOnDuplicateKey;
  private final boolean failOnTooBigNumber;
  private final PrintStream warningStream;

  private JSONParserErrorHandlingOptions(
      boolean showURI,
      boolean showLineAndColumnNumber,
      boolean showErrorLine,
      boolean failOnDuplicateKey,
      boolean failOnTooBigNumber,
      @Nullable PrintStream warningStream) {
    this.showURI = showURI;
    this.showLineAndColumnNumber = showLineAndColumnNumber;
    this.showErrorLine = showErrorLine;
    this.failOnDuplicateKey = failOnDuplicateKey;
    this.failOnTooBigNumber = failOnTooBigNumber;
    this.warningStream = warningStream;
  }

  /**
   * Returns whether an error message should show URI of a JSON text source instead of a short name.
   *
   * @return represents whether an error message should show URI
   */
  public boolean showURI() {
    return showURI;
  }

  /**
   * Returns whether an error message should show line and column number instead of character
   * position within a JSON text source
   *
   * @return represents whether an error message should show line and column number instead of
   *     character position within a JSON text source
   */
  public boolean showLineAndColumnNumber() {
    return showLineAndColumnNumber;
  }

  /**
   * Returns whether an error message should show actual JSON text line where the problme happened
   * with markers to indicate which character(s) have the problem.
   *
   * @return represents whether an error message should show actual JSON text line where the problem
   *     happened with markers to indicate which character(s) have the problem.
   */
  public boolean showErrorLine() {
    return showErrorLine;
  }

  /**
   * Returns whether the parse should throw an exception when it encounters duplicated keys in one
   * JSON object.
   *
   * @return whether the parse should throw an exception when it encounters duplicated keys
   */
  public boolean failOnDuplicateKey() {
    return failOnDuplicateKey;
  }

  /**
   * Returns whether the parse should throw an exception when there is a number that is not in the
   * range [-(2^53)+1, 2^53 - 1].
   *
   * <p>As indicated by <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6.
   * Numbers</a>, some JSON parser implementations may internally covert a number to a double
   * precision floating point value regardless whether it is an integer or not. To maximize the
   * interoperability, it is a good idea to keep the number within this range so that not
   * information loss occurs for integer values. This option enforces that policy to the users.
   *
   * @return whether the parse should throw an exception when there is a number that is not in the
   *     range [-(2^53)+1, 2^53-1].
   */
  public boolean failOnTooBigNumber() {
    return failOnTooBigNumber;
  }

  /**
   * Returns the stream where warnings should be printed.
   *
   * @return the stream where warnings should be printed. Null to print warnings nowhere.
   */
  @Nullable
  public PrintStream warningStream() {
    return warningStream;
  }

  /**
   * Returns a new builder of this class.
   *
   * @return a new builder of this class
   */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder class of {@link JSONParserErrorHandlingOptions}. */
  public static class Builder {
    private boolean showURI = false;
    private boolean showLineAndColumnNumber = true;
    private boolean showErrorLine = false;
    private boolean failOnDuplicateKey = false;
    private boolean failOnTooBigNumber = false;
    private PrintStream warningStream = System.err;

    private Builder() {}

    /**
     * Set {@link #showURI()} option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showURI(boolean b) {
      this.showURI = b;
      return this;
    }

    /**
     * Set {@link #showLineAndColumnNumber()} option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showLineAndColumnNumber(boolean b) {
      this.showLineAndColumnNumber = b;
      return this;
    }

    /**
     * Set {@link #showErrorLine()} option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showErrorLine(boolean b) {
      this.showErrorLine = b;
      return this;
    }

    /**
     * Set {@link #failOnDuplicateKey()} option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder failOnDuplicateKey(boolean b) {
      this.failOnDuplicateKey = b;
      return this;
    }

    /**
     * Set {@link #failOnTooBigNumber()} option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder failOnTooBigNumber(boolean b) {
      this.failOnTooBigNumber = b;
      return this;
    }

    /**
     * Set {@link #warningStream()} option.
     *
     * @param stream option value
     * @return this builder
     */
    public Builder warningStream(@Nullable PrintStream stream) {
      this.warningStream = stream;
      return this;
    }

    /**
     * Build a new instance of {@link JSONParserErrorHandlingOptions}.
     *
     * @return a new instance of {@link JSONParserErrorHandlingOptions}
     */
    public JSONParserErrorHandlingOptions build() {
      return new JSONParserErrorHandlingOptions(
          showURI,
          showLineAndColumnNumber,
          showErrorLine,
          failOnDuplicateKey,
          failOnTooBigNumber,
          warningStream);
    }
  }
}
