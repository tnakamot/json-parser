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
 * Thrown when a JSON lexical analyzer fails to tokenize the given JSON text.
 */
public class JSONLexerException extends Exception {
    private final JSONText source;
    private final int location;

    public JSONLexerException(JSONText source, int location, String msg) {
        super(msg);
        this.source   = source;
        this.location = location;

        // TODO: show an error message with line and column number.
    }

    public JSONText source() {
        return source;
    }

    public int location() {
        return location;
    }
}
