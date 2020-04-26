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
 * Represents one 'boolean" type token in JSON text.
 *
 * An instance of this class is immutable.
 */
public class JSONTokenBoolean extends JSONToken {
    public static final String JSON_TRUE  = "true";
    public static final String JSON_FALSE = "false";

    private final boolean value;

    protected JSONTokenBoolean(String text, StringLocation location, JSONText source) {
        super(JSONTokenType.BOOLEAN, text, location, source);
        if (JSON_TRUE.equals(text)) {
            this.value = true;
        } else if (JSON_FALSE.equals(text)) {
            this.value = false;
        } else {
            throw new IllegalArgumentException(
                    "text of boolean token must be either " + JSON_TRUE + " or " + JSON_FALSE);
        }
    }

    public boolean value() {
        return value;
    }
}
