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

package com.github.tnakamot.json.value;

import com.github.tnakamot.json.token.JSONTokenString;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Represents one JSON 'string' value.
 *
 * <p>Instances of this class are immutable.
 */
public class JSONValueString extends JSONValuePrimitive {
  private final String value;

  /**
   * Create an instance of a Java representation of a JSON string value.
   *
   * @param value value represented by a Java {@link String} object of this JSON string value. Null
   *     is considered as an empty string.
   */
  public JSONValueString(String value) {
    super(JSONValueType.STRING);
    this.value = (value == null) ? "" : value;
  }

  /**
   * Create an instance of a Java representation of a JSON string value from a JSON string token.
   *
   * @param token source token of this JSON string value.
   */
  public JSONValueString(JSONTokenString token) {
    super(JSONValueType.STRING, token);
    this.value = token.value();
  }

  /**
   * Return the value represented by a Java {@link String} object of this JSON string value.
   *
   * @return value represented by a Java {@link String} object of this JSON string value. Never be
   *     null.
   */
  public String value() {
    return value;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JSONValueString) {
      JSONValueString v = (JSONValueString) obj;
      return this.value.equals(v.value);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  @Override
  public String toString() {
    return value;
  }

  @Override
  @NotNull
  public String toTokenString() {
    if (token() == null) {
      return "\"" + StringEscapeUtils.escapeJson(value) + "\"";
    } else {
      return token().text();
    }
  }

  @Override
  @NotNull
  public String toTokenString(String newline, String indent) {
    return toTokenString();
  }
}
