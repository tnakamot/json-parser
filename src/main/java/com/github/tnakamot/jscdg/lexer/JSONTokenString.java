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

package com.github.tnakamot.jscdg.lexer;

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

    protected JSONTokenString(String text, String value, StringLocation location, JSONText source) {
        super(JSONTokenType.STRING, text, location, source);
        this.value = value;
    }

    /**
     * Returns the string value of this token represents. All escapes characters in the original
     * JSON text are unescaped.
     *
     * @return The string value of this token represents.
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-7">RFC 8259 - 7. Strings</a>
     */
    public String value() {
        return value;
    }
}
