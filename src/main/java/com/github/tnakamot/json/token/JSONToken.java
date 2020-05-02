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
 * Represents one token in JSON text.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONToken {
    public static final String JSON_BEGIN_ARRAY     = "[";
    public static final String JSON_END_ARRAY       = "]";
    public static final String JSON_BEGIN_OBJECT    = "{";
    public static final String JSON_END_OBJECT      = "}";
    public static final String JSON_NAME_SEPARATOR  = ":";
    public static final String JSON_VALUE_SEPARATOR = ",";

    private final JSONTokenType type;
            final String        text;
    private final StringLocation begin;
    private final StringLocation end;
    private final JSONText source;

    /**
     * Create one JSON text token.
     *
     * @param type   type of this token
     * @param text   text of this token
     * @param begin  beginning location of this token within the source JSON text
     * @param end    end location of this token within the source JSON text
     * @param source source JSON text where this token was extracted from
     */
    public JSONToken(JSONTokenType type, String text,
                     StringLocation begin, StringLocation end, JSONText source) {
        this.type   = type;
        this.text   = text;
        this.begin  = begin;
        this.end    = end;
        this.source = source;

        if (type == null)
            throw new NullPointerException("type cannot be null");
        if (text == null)
            throw new NullPointerException("text cannot be null");
        if (begin == null)
            throw new NullPointerException("begin cannot be null");
        if (end == null)
            throw new NullPointerException("end cannot be null");
        if (source == null)
            throw new NullPointerException("source cannot be null");
    }

    /**
     * Type of this token.
     *
     * @return type of this token
     */
    public JSONTokenType type() {
        return type;
    }

    /**
     * Text representation of this token as it appears in the source JSON text.
     *
     * @return text representation of this token as it appears in the source JSON text.
     */
    public String text() {
        return text;
    }

    /**
     * Location of the beginning of the token within the source JSON text.
     *
     * @return location of the beginning of the token within the source JSON text.
     */
    public StringLocation beginningLocation() {
        return begin;
    }

    /**
     * Location of the end of the token within the source JSON text.
     *
     * @return location of the end of the token within the source JSON text.
     */
    public StringLocation endLocation() {
        return end;
    }

    /**
     * Source JSON text where this token was extracted from.
     *
     * @return source JSON text where this token was extracted from.
     */
    public JSONText source() {
        return source;
    }
}
