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
import org.jetbrains.annotations.NotNull;

/**
 * Represents one JSON 'array' value (immutable).
 *
 * <p>
 * Instances of this class are immutable. Any method call that may result in
 * the modification of the array (e.g. {@link #add(JSONValue)} results in
 * {@link UnsupportedOperationException}.
 *
 * @see JSONValueArrayMutable
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
    @NotNull
    public Iterator<JSONValue> iterator() {
        return values.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public Object[] toArray() {
        return values.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public <T> T[] toArray(T[] ts) {
        return values.toArray(ts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean add(JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot add a value");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("cannot remove a value");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(@NotNull Collection collection) {
        return values.containsAll(collection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean addAll(@NotNull Collection collection) {
        throw new UnsupportedOperationException("cannot add values");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean addAll(int i, @NotNull Collection collection) {
        throw new UnsupportedOperationException("cannot add values");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException("cannot clear values");
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
    @Deprecated
    public JSONValue set(int i, JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot set a value");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public void add(int i, JSONValue jsonValue) {
        throw new UnsupportedOperationException("cannot add a value");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public JSONValue remove(int i) {
        throw new UnsupportedOperationException("cannot remove a value");
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
    @NotNull
    public ListIterator<JSONValue> listIterator() {
        return values.listIterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public ListIterator<JSONValue> listIterator(int i) {
        return values.listIterator(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @NotNull
    public List<JSONValue> subList(int i, int i1) {
        return values.subList(i, i1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean retainAll(@NotNull Collection<?> collection) {
        throw new UnsupportedOperationException("retainAll is not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Deprecated
    public boolean removeAll(@NotNull Collection<?> collection) {
        throw new UnsupportedOperationException("cannot remove values");
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof JSONValueArrayImmutable) {
            return values.equals(((JSONValueArrayImmutable) o).values);
        } else {
            return false;
        }
    }

    /**
     * @return a mutable version of the same JSON array.
     */
    public JSONValueArrayMutable toMutable() {
        return new JSONValueArrayMutable(this);
    }
}
