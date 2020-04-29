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

/**
 * Represents one JSON value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public abstract class JSONValue {
    private final JSONValueType type;

    /**
     * Create an instance of a Java representation of a JSON value.
     *
     * @param type type of this JSON value
     */
    JSONValue(JSONValueType type) {
        this.type = type;
    }

    /**
     * Type of this JSON value.
     *
     * @return type of this JSON value
     */
    public JSONValueType type() {
        return type;
    }
}
