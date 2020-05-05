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

package com.github.tnakamot.json.token;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.value.JSONValueNumber;

/**
 * Represents one "number" type token in JSON text.
 *
 * <p>The representation of a number in JSON is defined by <a
 * href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>. This RFC
 * specification does not set limits of the range and precision of numbers. Although the
 * specification allows software implementations to set limits, this implementation does not
 * practically set limits. An application program can get the original text that represents this
 * number without loosing precision by calling {@link #text()} to maximize the interoperability.
 *
 * <p>Instances of this class are immutable.
 */
public class JSONTokenNumber extends JSONToken {
  /**
   * Creates one "number" type token of a JSON text.
   *
   * <p>It is the caller's responsibility to validate the token text as number before creating this
   * instance.
   *
   * @param text text of this token
   * @param begin beginning location of this token within the source JSON text
   * @param end end location of this token within the source JSON text
   * @param source source JSON text where this token was extracted from
   */
  public JSONTokenNumber(String text, StringLocation begin, StringLocation end, JSONText source) {
    super(JSONTokenType.NUMBER, text, begin, end, source);

    if (!text.matches(JSONValueNumber.NUMBER_PATTERN)) {
      throw new NumberFormatException(
          "The given text '"
              + text
              + "' does not match the pattern of JSON numbers defined in RFC 8259.");
    }
  }

  /**
   * Text representation of this token as it appears in the source JSON text.
   *
   * <p>The returned text always complies with <a
   * href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>.
   *
   * @return text representation of this token as it appears in the source JSON text.
   */
  public String text() {
    return text;
  }
}
