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

import com.github.tnakamot.json.token.JSONToken;
import com.github.tnakamot.json.token.JSONTokenNull;
import org.jetbrains.annotations.NotNull;

/**
 * Represents one JSON 'null' value.
 *
 * <p>Instances of this class are immutable.
 */
public class JSONValueNull extends JSONValuePrimitive {
  /** An instance of null value without token information. */
  public static final JSONValueNull INSTANCE = new JSONValueNull();

  /** Create an instance of a Java representation of a JSON null value. */
  private JSONValueNull() {
    this(null);
  }

  /**
   * Create an instance of a Java representation of a JSON null value with source JSON text
   * information.
   *
   * @param token source of this JSON null value. Can be null if this JSON null value is not
   *     originated from an exiting JSON text.
   */
  public JSONValueNull(JSONToken token) {
    super(JSONValueType.NULL, token);
  }

  @Override
  public int hashCode() {
    return JSONTokenNull.JSON_NULL.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof JSONValueNull;
  }

  @Override
  public String toString() {
    return JSONTokenNull.JSON_NULL;
  }

  @Override
  @NotNull
  public String toTokenString() {
    return JSONTokenNull.JSON_NULL;
  }

  @Override
  @NotNull
  public String toTokenString(String newline, String indent) {
    return toTokenString();
  }
}
