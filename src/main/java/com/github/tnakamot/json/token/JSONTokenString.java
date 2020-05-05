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

/**
 * Represents one "string" type token in JSON text.
 *
 * <p>
 * A string token in a JSON text is surrounded by double quotations, and some special characters
 * are unescaped. The method {@link #value()} strips the surrounding double quotations, unescape
 * the escaped characters and returns in accordance with
 * <a href="https://tools.ietf.org/html/rfc8259#section-7">RFC 8259 - 7. Strings</a>.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONTokenString extends JSONToken {
    private final String value;

    /**
     * Creates one "string" type token of a JSON text.
     *
     * <p>
     * It is the caller's responsibility to validate the token text as string
     * before creating this instance.
     *
     * @param text   text of this token
     * @param value  string value that this token represents
     *               (it is a caller's responsibility to parse the token text,
     *               strip surrounding double quotations and unescape escaped
     *               characters)
     * @param begin  beginning location of this token within the source JSON text
     * @param end    end location of this token within the source JSON text
     * @param source source JSON text where this token was extracted from
     */
    public JSONTokenString(String text, String value,
                           StringLocation begin, StringLocation end, JSONText source) {
        super(JSONTokenType.STRING, text, begin, end, source);
        this.value = value;

        if (value == null) {
            throw new NullPointerException("value cannot be null");
        }
    }

    /**
     * Returns the string value of this token represents. All escapes characters in the original
     * JSON text are unescaped.
     *
     * @return The string value of this token represents. Never be null.
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-7">RFC 8259 - 7. Strings</a>
     */
    public String value() {
        return value;
    }
}
