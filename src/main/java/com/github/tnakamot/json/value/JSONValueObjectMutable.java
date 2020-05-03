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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Represents one JSON 'object' value (mutable).
 *
 * <p>
 * This implementation retains the order.
 *
 * <p>
 * TODO: write unit tests
 *
 * @see JSONValueObjectImmutable
 */
public class JSONValueObjectMutable extends JSONValueObject {
    private final LinkedHashMap<JSONValueString, JSONValue> members;

    /**
     * Create an instance of a Java representation of an empty JSON 'object' value.
     */
    public JSONValueObjectMutable() {
        this.members = new LinkedHashMap<>();
    }

    /**
     * Create an instance of a Java representation of a JSON 'object' value.
     *
     * @param members name/value pairs. Null is considered as an empty object.
     */
    JSONValueObjectMutable(Map<JSONValueString, JSONValue> members) {
        if (members == null) {
            this.members = new LinkedHashMap<>();
        } else {
            this.members = new LinkedHashMap<>(members);
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return members.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return members.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object o) {
        return members.containsKey(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(String s) {
        return members.containsKey(new JSONValueString(s));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object o) {
        return members.containsValue(o);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue put(JSONValueString jsonValueString, JSONValue jsonValue) {
        return members.put(jsonValueString, jsonValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue remove(Object o) {
        return members.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(@NotNull Map<? extends JSONValueString, ? extends JSONValue> map) {
        members.putAll(map);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        members.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Set<JSONValueString> keySet() {
        return members.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Collection<JSONValue> values() {
        return members.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Set<Entry<JSONValueString, JSONValue>> entrySet() {
        return members.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return members.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof JSONValueObjectMutable) {
            return members.equals(((JSONValueObjectMutable) o).members);
        } else {
            return false;
        }
    }

    /**
     * @return an immutable version of the same JSON object.
     */
    public JSONValueObjectImmutable toImmutable() {
        return new JSONValueObjectImmutable(this);
    }
}
