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

import java.util.*;

/**
 * Represents one JSON 'array' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueArray extends JSONValue implements List<JSONValue> {
    private List<JSONValue> values;

    /**
     * Create an instance of a Java representation of a JSON 'array' value.
     *
     * @param values sequence of values
     */
    public JSONValueArray(List<JSONValue> values) {
        super(JSONValueType.ARRAY);
        this.values = new ArrayList<JSONValue>(values);

        if (values == null) {
            throw new NullPointerException("values cannot be null");
        }
    }

    /**
     * A copy of the sequence of values.
     *
     * @return Copy of the sequence of values.
     */
    public List<JSONValue> list() {
        return new ArrayList<JSONValue>(values);
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
    public Iterator iterator() {
        return values.iterator();
    }

    @Override
    public Object[] toArray() {
        return values.toArray();
    }

    @Override
    public boolean add(JSONValue jsonValue) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("cannot remove a value");
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
        return null;
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
    public ListIterator listIterator() {
        return values.listIterator();
    }

    @Override
    public ListIterator listIterator(int i) {
        return values.listIterator(i);
    }

    @Override
    public List subList(int i, int i1) {
        return values.subList(i, i1);
    }

    @Override
    public boolean retainAll(Collection collection) {
        return values.retainAll(collection);
    }

    @Override
    public boolean removeAll(Collection collection) {
        throw new UnsupportedOperationException("cannot remove values");
    }

    @Override
    public boolean containsAll(Collection collection) {
        return values.containsAll(collection);
    }

    @Override
    public Object[] toArray(Object[] objects) {
        return values.toArray(objects);
    }
}
