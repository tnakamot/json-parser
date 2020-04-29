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

package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONToken;

/**
 * Represents one JSON 'null' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueNull extends JSONValuePrimitive {
    /**
     * Create an instance of a Java representation of a JSON null value.
     */
    public JSONValueNull() {
        this(null);
    }

    /**
     * Create an instance of a Java representation of a JSON null value
     * with source JSON text information.
     *
     * @param token source of this JSON null value. Can be null if this JSON
     *              null value is not originated from an exiting JSON text.
     */
    public JSONValueNull(JSONToken token) {
        super(JSONValueType.NULL, token);
    }

    @Override
    public String toString() {
        return "null";
    }
}
