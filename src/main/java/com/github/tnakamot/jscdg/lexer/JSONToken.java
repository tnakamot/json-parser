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
 * Represents one token in JSON text.
 *
 * An instance of this class is immutable.
 */
public class JSONToken {
    private final JSONTokenType type;
    private final String        text;
    private final int           location;
    private final JSONText      source;

    /**
     * Create one JSON token.
     *
     * @param type     type of this token
     * @param text     text of this token
     * @param location location of this token within the source JSON text (see {@link #location()}
     * @param source   source JSON text where this token was extracted from
     */
    protected JSONToken(JSONTokenType type, String text, int location, JSONText source) {
        this.type     = type;
        this.text     = text;
        this.location = location;
        this.source   = source;

        if (type == null)
            throw new NullPointerException("type cannot be null");
        if (text == null)
            throw new NullPointerException("text cannot be null");
        if (location < 0)
            throw new IllegalArgumentException("location must be positive");
        if (source == null)
            throw new NullPointerException("source cannot be null");
    }

    /**
     * @return type of this token
     */
    public JSONTokenType type() {
        return type;
    }

    /**
     * @return text of this token.
     */
    public String text() {
        return text;
    }

    /**
     * Returns the location of the beginning of the token text within the source JSON text {@link #source()}.
     * For example, if the source text is
     *
     * <pre>
     *     { "key": "value" }
     * </pre>
     *
     * , the location of the token "key" is 2.
     *
     * <p>
     * The location is counted based on Unicode code units, which means that a surrogate pair is counted
     * as two characters.
     *
     * @return the location of the beginning of the token within the source JSON text.
     */
    public int location() {
        return location;
    }

    /**
     * The source JSON text where this token was extracted from.
     *
     * @return An instance of JSON text.
     */
    public JSONText source() {
        return source;
    }

    /* TODO: overwrite toString */
}
