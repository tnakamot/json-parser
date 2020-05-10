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

/**
 * A data structure to hold the switches to change the error message format of {@link
 * JSONParserException}. See {@link JSONParserException} for more details about how the error
 * messages are formatted.
 *
 * <p>Instances of this class are immutable.
 *
 * <p>This class follows Builder Pattern. To make an instance of this class, use the code snippet
 * something like below:
 *
 * <pre>
 * JSONLexerErrorMessageFormat errMsgFmt =
 *     JSONLexerErrorMessageConfiguration.builder()
 *         .showURI(false)
 *         .showLineAndColumnNumber(true)
 *         .showErrorLine(false)
 *         .build();
 * </pre>
 *
 * <p>The invocation of {@link Builder#showURI()}, {@link Builder#showLineAndColumnNumber()} and
 * {@link Builder#showErrorLine()} are optional. If you want to use the default configuration, just
 * do not call them.
 *
 * <p>The default value may change in the future, so do not rely on them if you really need a
 * specific error message format.
 */
public class JSONParserErrorMessageFormat {
  private final boolean showURI;
  private final boolean showLineAndColumnNumber;
  private final boolean showErrorLine;

  private JSONParserErrorMessageFormat(
      boolean showURI, boolean showLineAndColumnNumber, boolean showErrorLine) {
    this.showURI = showURI;
    this.showLineAndColumnNumber = showLineAndColumnNumber;
    this.showErrorLine = showErrorLine;
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
   * @return represents whether an error message should show line and column number instead of
   *     character position within a JSON text source
   */
  public boolean showLineAndColumnNumber() {
    return showLineAndColumnNumber;
  }

  /**
   * @return represents whether an error message should show actual JSON text line where the problem
   *     happened with the arrow to indicate which character has the problem.
   */
  public boolean showErrorLine() {
    return showErrorLine;
  }

  /** @return a new builder of this class */
  public static Builder builder() {
    return new Builder();
  }

  /** Builder class of {@link JSONParserErrorMessageFormat}. */
  public static class Builder {
    private boolean showURI = false;
    private boolean showLineAndColumnNumber = true;
    private boolean showErrorLine = false;

    private Builder() {}

    /**
     * Set showURI option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showURI(boolean b) {
      this.showURI = b;
      return this;
    }

    /**
     * Set showLineAndColumnNumber option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showLineAndColumnNumber(boolean b) {
      this.showLineAndColumnNumber = b;
      return this;
    }

    /**
     * Set showErrorLine option.
     *
     * @param b option value
     * @return this builder
     */
    public Builder showErrorLine(boolean b) {
      this.showErrorLine = b;
      return this;
    }

    /**
     * Build a new instance of {@link JSONParserErrorMessageFormat}.
     *
     * @return a new instance of {@link JSONParserErrorMessageFormat}
     */
    public JSONParserErrorMessageFormat build() {
      return new JSONParserErrorMessageFormat(showURI, showLineAndColumnNumber, showErrorLine);
    }
  }
}
