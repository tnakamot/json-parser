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
 * Represents one JSON 'array' value (mutable).
 *
 * <p>
 * This is a mutable version of {@link JSONValueArray}.
 *
 * @see {@link JSONValueArrayImmutable}
 */
public class JSONValueArrayMutable extends JSONValueArray {
    private final List<JSONValue> values;

    /**
     * Create an instance of a Java representation of an empty JSON 'array' value.
     */
    public JSONValueArrayMutable() {
        this.values = new ArrayList<>();
    }

    /**
     * Create an instance of a Java representation of a JSON 'array' value.
     *
     * @param values sequence of values. Null is considered as an empty array.
     */
    public JSONValueArrayMutable(List<JSONValue> values) {
        if (values == null) {
            this.values = new ArrayList<>();
        } else {
            this.values = new ArrayList<>(values);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return values.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<JSONValue> iterator() {
        return values.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean add(JSONValue jsonValue) {
        return values.add(jsonValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return values.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(Collection collection) {
        return values.addAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addAll(int i, Collection collection) {
        return values.addAll(i, collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        values.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue get(int i) {
        return values.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue set(int i, JSONValue jsonValue) {
        return values.set(i, jsonValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(int i, JSONValue jsonValue) {
        values.add(i, jsonValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONValue remove(int i) {
        return values.remove(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<JSONValue> listIterator() {
        return values.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListIterator<JSONValue> listIterator(int i) {
        return values.listIterator(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JSONValue> subList(int i, int i1) {
        return values.subList(i, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection collection) {
        return values.retainAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection collection) {
        return values.removeAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection collection) {
        return values.containsAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] ts) {
        return values.toArray(ts);
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return values.equals(o);
    }

    /**
     * @return an immutable version of the same JSON array.
     */
    public JSONValueArrayImmutable toImmutable() {
        return new JSONValueArrayImmutable(this);
    }
}
