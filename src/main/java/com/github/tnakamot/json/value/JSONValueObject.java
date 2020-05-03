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
import java.util.Map;
import java.util.Set;

/**
 * Represents one JSON 'object' value.
 */
public abstract class JSONValueObject extends JSONValue implements Map<JSONValueString, JSONValue> {
    /**
     * Create an instance of a Java representation of a JSON 'object' value.
     */
    JSONValueObject() {
        super(JSONValueType.OBJECT);
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
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * Returns the JSON value which has the given name.
     *
     * @param name name
     * @return a JSON value of the given name
     * @see #get(JSONValueString)
     */
    public JSONValue get(String name) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object o) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * Check if this JSON object has a value which has the given name.
     *
     * @param name name
     * @return true if a value with the given name exists
     * @see #containsKey(Object)
     */
    public boolean containsKey(String name) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object o) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue get(Object o) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue put(JSONValueString jsonValueString, JSONValue jsonValue) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue remove(Object o) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends JSONValueString, ? extends JSONValue> map) {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<JSONValueString> keySet() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<JSONValue> values() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<JSONValueString, JSONValue>> entrySet() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("need to be overridden");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("need to be overridden");
    }
}
