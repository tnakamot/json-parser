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

import com.github.tnakamot.json.token.JSONTokenBoolean;

/**
 * Represents one JSON 'boolean' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueBoolean extends JSONValuePrimitive {
    public static final JSONValueBoolean TRUE = new JSONValueBoolean(true);
    public static final JSONValueBoolean FALSE = new JSONValueBoolean(false);

    private final boolean value;

    /**
     * Return an instance of a Java representation of a JSON boolean value.
     *
     * @param value value represented by Java boolean primitive of this
     *              JSON boolean value.
     */
    public static JSONValueBoolean valueOf(boolean value) {
        return value ? TRUE : FALSE;
    }

    /**
     * Create an instance of a Java representation of a JSON boolean value.
     *
     * @param value value represented by Java boolean primitive of this
     *              JSON boolean value.
     */
    private JSONValueBoolean(boolean value) {
        super(JSONValueType.BOOLEAN, null);
        this.value = value;
    }

    /**
     * Create an instance of a Java representation of a JSON boolean value
     * from a JSON boolean token.
     *
     * @param token source token of this JSON boolean value.
     */
    public JSONValueBoolean(JSONTokenBoolean token) {
        super(JSONValueType.BOOLEAN, token);
        this.value = token.value();
    }

    /**
     * Return the value represented by Java boolean primitives of this
     * JSON boolean value.
     *
     * @return value represented by Java boolean primitives of this JSON boolean value
     */
    public boolean value() {
        return value;
    }

    @Override
    public int hashCode() {
        return Boolean.valueOf(value).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONValueBoolean) {
            JSONValueBoolean b = (JSONValueBoolean) obj;
            return this.value == b.value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
}