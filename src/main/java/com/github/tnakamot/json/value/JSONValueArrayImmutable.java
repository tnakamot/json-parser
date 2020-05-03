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
 * Represents one JSON 'array' value (immutable).
 *
 * <p>
 * Instances of this class are immutable. Any method call that may result in
 * the modification of the array (e.g. {@link #add(JSONValue)} results in
 * {@link UnsupportedOperationException}.
 *
 * @see {@link JSONValueArrayMutable}
 */
public class JSONValueArrayImmutable extends JSONValueArray {
    private final List<JSONValue> values;

    /**
     * Create an instance of a Java representation of a JSON 'array' value.
     *
     * @param values sequence of values. Null is considered as an empty array.
     */
    JSONValueArrayImmutable(List<JSONValue> values) {
        if (values == null) {
            this.values = new ArrayList<>();
        } else {
            this.values = new ArrayList<>(values);
        }
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return values.contains(o);
    }

    @Override
    public Iterator<JSONValue> iterator() {
        return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return values.toArray(ts);
    }

    @Override
    public boolean add(JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot add a value");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("cannot remove a value");
    }

    @Override
    public boolean containsAll(Collection collection) {
        return values.containsAll(collection);
    }

    @Override
    public boolean addAll(Collection collection) {
        throw new UnsupportedOperationException("cannot add values");
    }

    @Override
    public boolean addAll(int i, Collection collection) {
        throw new UnsupportedOperationException("cannot add values");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("cannot clear values");
    }

    @Override
    public JSONValue get(int i) {
        return values.get(i);
    }

    @Override
    public JSONValue set(int i, JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot set a value");
    }

    @Override
    public void add(int i, JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot add a value");
    }

    @Override
    public JSONValue remove(int i) {
        throw new UnsupportedOperationException("cannot remove a value");
    }

    @Override
    public int indexOf(Object o) {
        return values.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return values.lastIndexOf(o);
    }

    @Override
    public ListIterator<JSONValue> listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator<JSONValue> listIterator(int i) {
        return values.listIterator(i);
    }

    @Override
    public List<JSONValue> subList(int i, int i1) {
        return values.subList(i, i1);
    }

    @Override
    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException("retainAll is not supported");
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("cannot remove values");
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
     * @return a mutable version of the same JSON array.
     */
    public JSONValueArrayMutable toMutable() {
        return new JSONValueArrayMutable(this);
    }
}
