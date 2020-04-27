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
 * Represents one "null" token in JSON text.
 *
 * <p>
 * Based on <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>, the token text returned
 * by {@link #text()} is always "null".
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONTokenNull extends JSONToken {
    public static final String JSON_NULL  = "null";

    protected JSONTokenNull(StringLocation location, JSONText source) {
        super(JSONTokenType.NULL, JSON_NULL, location, source);
    }
}
