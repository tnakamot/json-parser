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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represents one JSON 'object' value.
 *
 * <p>
 * This implementation retains the order.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueObject extends JSONValue implements Map<JSONValueString, JSONValue> {
    private final LinkedHashMap<JSONValueString, JSONValue> members;

    /**
     * Create an instance of a Java representation of a JSON 'object' value.
     *
     * @param members name/value pairs
     */
    public JSONValueObject(Map<JSONValueString, JSONValue> members) {
        super(JSONValueType.OBJECT);
        this.members = new LinkedHashMap<>(members);

        if (members == null) {
            throw new NullPointerException("members cannot be null");
        }
    }

    /**
     * A copy of name/value pairs.
     *
     * @return Copy of name/value pairs.
     */
    public Map<JSONValueString, JSONValue> toMap() {
        return new LinkedHashMap<>(members);
    }

    /**
     * Returns the JSON value of the given name.
     *
     * <p>
     * If a value of the given name does not exist, this method
     * returns null. Do not confuse it with a JSON 'null' value.
     * If there is a JSON 'null' value of the given name, this
     * method returns an instance of {@link JSONValueNull} instead.
     *
     * @param name a JSON string value which represents a name
     * @return a JSON value of the given name
     */
    public JSONValue get(JSONValueString name) {
        return members.get(name);
    }

    /**
     * Returns the JSON value which has the given name.
     *
     * @param name name
     * @return a JSON value of the given name
     * @see #get(JSONValueString)
     */
    public JSONValue get(String name) {
        return members.get(new JSONValueString(name));
    }

    @Override
    public int size() {
        return members.size();
    }

    @Override
    public boolean isEmpty() {
        return members.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return members.containsKey(o);
    }

    public boolean containsKey(String s) {
        return members.containsKey(new JSONValueString(s));
    }

    @Override
    public boolean containsValue(Object o) {
        return members.containsValue(o);
    }

    @Override
    public JSONValue get(Object o) {
        if (o instanceof JSONValueString) {
            return get((JSONValueString) o);
        } else if (o instanceof String) {
            return get((String) o);
        } else {
            return null;
        }
    }

    @Override
    public JSONValue put(JSONValueString jsonValueString, JSONValue jsonValue) {
        return null;
    }

    @Override
    public JSONValue remove(Object o) {
        throw new UnsupportedOperationException("cannot be removed");
    }

    @Override
    public void putAll(Map<? extends JSONValueString, ? extends JSONValue> map) {
        throw new UnsupportedOperationException("cannot put values");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("cannot clear");
    }

    @Override
    public Set<JSONValueString> keySet() {
        return members.keySet();
    }

    @Override
    public Collection<JSONValue> values() {
        return members.values();
    }

    @Override
    public Set<Entry<JSONValueString, JSONValue>> entrySet() {
        return members.entrySet();
    }

    // TODO: override hashCode() and equals(Object)
}
