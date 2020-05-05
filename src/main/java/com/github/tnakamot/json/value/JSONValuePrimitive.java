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

package com.github.tnakamot.json.value;

import com.github.tnakamot.json.token.JSONToken;

/**
 * Represents one JSON primitive value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public abstract class JSONValuePrimitive extends JSONValue {
    private final JSONToken token;

    /**
     * Create an instance of a Java representation of a JSON primitive value.
     *
     * @param type type of this JSON value
     */
    JSONValuePrimitive(JSONValueType type) {
        this(type, null);
    }

    /**
     * Create an instance of a Java representation of a JSON primitive value
     * with source JSON text information.
     *
     * @param type  type of this JSON value
     * @param token source of this JSON value. Can be null if this JSON value
     *              is not originated from an exiting JSON text.
     */
    JSONValuePrimitive(JSONValueType type, JSONToken token) {
        super(type);
        this.token = token;
    }


    /**
     * The source token of this JSON value.
     *
     * @return the source token of this JSON value. Can be null if this JSON value
     * is not originated from an existing JSON text.
     */
    public JSONToken token() {
        return token;
    }
}
