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
 * Represents one 'string' type token in JSON text.
 *
 * An instance of this class is immutable.
 */
public class JSONTokenString extends JSONToken {
    private final String value;

    protected JSONTokenString(String text, String value, int location, JSONText source) {
        super(JSONTokenType.STRING, text, location, source);
        this.value = value;
    }

    public String value() {
        return value;
    }
}
