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

import java.util.*;

/**
 * Represents one JSON 'array' value.
 */
public abstract class JSONValueArray extends JSONValue implements List<JSONValue> {
    /**
     * Create an instance of a Java representation of a JSON 'array' value.
     */
    JSONValueArray() {
        super(JSONValueType.ARRAY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int size();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean isEmpty();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean contains(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Iterator<JSONValue> iterator();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Object[] toArray();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean add(JSONValue jsonValue);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean remove(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean addAll(Collection collection);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean addAll(int i, Collection collection);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void clear();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract JSONValue get(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract JSONValue set(int i, JSONValue jsonValue);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void add(int i, JSONValue jsonValue);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract JSONValue remove(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int indexOf(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int lastIndexOf(Object o);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ListIterator<JSONValue> listIterator();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract ListIterator<JSONValue> listIterator(int i);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract List<JSONValue> subList(int i, int i1);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean retainAll(Collection collection);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean removeAll(Collection collection);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean containsAll(Collection collection);

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract <T> T[] toArray(T[] ts);
}
