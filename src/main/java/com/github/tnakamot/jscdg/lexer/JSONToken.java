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
 * <p>
 * Instances of this class are immutable.
 */
public class JSONToken {
    private final JSONTokenType type;
    private final String        text;
    private final StringLocation location;
    private final JSONText      source;

    /**
     * Create one JSON token.
     *
     * @param type     type of this token
     * @param text     text of this token
     * @param location location of this token within the source JSON text
     * @param source   source JSON text where this token was extracted from
     */
    protected JSONToken(JSONTokenType type, String text, StringLocation location, JSONText source) {
        this.type     = type;
        this.text     = text;
        this.location = location;
        this.source   = source;

        if (type == null)
            throw new NullPointerException("type cannot be null");
        if (text == null)
            throw new NullPointerException("text cannot be null");
        if (location == null)
            throw new NullPointerException("location cannot be null");
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
     * @return text of this token as it appears in the source JSON text.
     */
    public String text() {
        return text;
    }

    /**
     * @return the location of the beginning of the token within the source JSON text.
     */
    public StringLocation location() {
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
}
