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

import org.jetbrains.annotations.NotNull;

import java.util.*;

/** Represents one JSON 'array' value. */
public abstract class JSONValueArray extends JSONValue implements List<JSONValue> {
  /** Create an instance of a Java representation of a JSON 'array' value. */
  JSONValueArray() {
    super(JSONValueType.ARRAY);
  }

  /** {@inheritDoc} */
  @Override
  public abstract int size();

  /** {@inheritDoc} */
  @Override
  public abstract boolean isEmpty();

  /** {@inheritDoc} */
  @Override
  public abstract boolean contains(Object o);

  /** {@inheritDoc} */
  @Override
  @NotNull
  public abstract Iterator<JSONValue> iterator();

  /** {@inheritDoc} */
  @Override
  public abstract Object[] toArray();

  /** {@inheritDoc} */
  @Override
  public abstract boolean add(JSONValue jsonValue);

  /** {@inheritDoc} */
  @Override
  public abstract boolean remove(Object o);

  /** {@inheritDoc} */
  @Override
  public abstract boolean addAll(@NotNull Collection<? extends JSONValue> collection);

  /** {@inheritDoc} */
  @Override
  public abstract boolean addAll(int i, @NotNull Collection<? extends JSONValue> collection);

  /** {@inheritDoc} */
  @Override
  public abstract void clear();

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue get(int i);

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue set(int i, JSONValue jsonValue);

  /** {@inheritDoc} */
  @Override
  public abstract void add(int i, JSONValue jsonValue);

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue remove(int i);

  /** {@inheritDoc} */
  @Override
  public abstract int indexOf(Object o);

  /** {@inheritDoc} */
  @Override
  public abstract int lastIndexOf(Object o);

  /** {@inheritDoc} */
  @Override
  @NotNull
  public abstract ListIterator<JSONValue> listIterator();

  /** {@inheritDoc} */
  @Override
  @NotNull
  public abstract ListIterator<JSONValue> listIterator(int i);

  /** {@inheritDoc} */
  @Override
  @NotNull
  public abstract List<JSONValue> subList(int i, int i1);

  /** {@inheritDoc} */
  @Override
  public abstract boolean retainAll(@NotNull Collection<?> collection);

  /** {@inheritDoc} */
  @Override
  public abstract boolean removeAll(@NotNull Collection<?> collection);

  /** {@inheritDoc} */
  @Override
  public abstract boolean containsAll(@NotNull Collection<?> collection);

  /** {@inheritDoc} */
  @Override
  public abstract <T> T[] toArray(T[] ts);

  /**
   * Add a JSON boolean value to this array.
   *
   * @param value boolean value to add
   * @return true (as specified by {@link Collection#add(Object)}
   */
  public boolean add(boolean value) {
    return add(JSONValueBoolean.valueOf(value));
  }

  /**
   * Add a JSON number value to this array.
   *
   * @param value a number value to add
   * @return true (as specified by {@link Collection#add(Object)}
   */
  public boolean add(long value) {
    return add(new JSONValueNumber(value));
  }

  /**
   * Add a JSON number value to this array.
   *
   * @param value a number value to add
   * @return true (as specified by {@link Collection#add(Object)}
   */
  public boolean add(double value) {
    return add(new JSONValueNumber(value));
  }

  /**
   * Add a JSON string value to this array.
   *
   * @param value a String value to add. Null is considered as an empty string.
   * @return true (as specified by {@link Collection#add(Object)}
   */
  public boolean add(String value) {
    return add(new JSONValueString(value));
  }

  /**
   * Returns a boolean value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the boolean value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;=
   *     size())
   * @throws WrongValueTypeException if the value type at the specified index is not boolean
   */
  public boolean getBoolean(int index) throws IndexOutOfBoundsException, WrongValueTypeException {
    JSONValue val = get(index);
    if (val instanceof JSONValueBoolean) {
      return ((JSONValueBoolean) val).value();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.BOOLEAN, val.type());
    }
  }

  /**
   * Returns a number value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the long value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;=
   *     size())
   * @throws WrongValueTypeException if the value type at the specified index is not number
   * @throws NumberFormatException if the value cannot be converted to a Java long value
   */
  public long getLong(int index)
      throws IndexOutOfBoundsException, WrongValueTypeException, NumberFormatException {
    JSONValue val = get(index);
    if (val instanceof JSONValueNumber) {
      return ((JSONValueNumber) val).toLong();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.NUMBER, val.type());
    }
  }

  /**
   * Returns a number value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the double value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;=
   *     size())
   * @throws WrongValueTypeException if the value type at the specified index is not number
   */
  public double getDouble(int index)
      throws IndexOutOfBoundsException, WrongValueTypeException, NumberFormatException {
    JSONValue val = get(index);
    if (val instanceof JSONValueNumber) {
      return ((JSONValueNumber) val).toDouble();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.NUMBER, val.type());
    }
  }

  /**
   * Returns a string value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the string value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;=
   *     size())
   * @throws WrongValueTypeException if the value type at the specified index is not string
   */
  public String getString(int index) throws IndexOutOfBoundsException, WrongValueTypeException {
    JSONValue val = get(index);
    if (val instanceof JSONValueString) {
      return ((JSONValueString) val).value();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.STRING, val.type());
    }
  }

  /**
   * Returns an array value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the array value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
   * @throws WrongValueTypeException if the value type at the specified index is not array
   */
  public JSONValueArray getArray(int index)
      throws IndexOutOfBoundsException, WrongValueTypeException {
    JSONValue val = get(index);
    if (val instanceof JSONValueArray) {
      return (JSONValueArray) val;
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.ARRAY, val.type());
    }
  }

  /**
   * Returns an object value at the specified position in this array.
   *
   * @param index index of the value to return
   * @return the object value at the specified position in this array
   * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
   * @throws WrongValueTypeException if the value type at the specified index is not object
   */
  public JSONValueObject getObject(int index)
      throws IndexOutOfBoundsException, WrongValueTypeException {
    JSONValue val = get(index);
    if (val instanceof JSONValueObject) {
      return (JSONValueObject) val;
    } else {
      throw new WrongValueTypeException(
          "Wrong value type at index " + index + ".", JSONValueType.OBJECT, val.type());
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (JSONValue val : this) {
      hash += val.hashCode();
    }
    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof JSONValueArray)) {
      return false;
    }
    JSONValueArray a = (JSONValueArray) o;
    if (size() != a.size()) {
      return false;
    }

    for (int i = 0; i < size(); i++) {
      JSONValue v1 = get(i);
      JSONValue v2 = a.get(i);
      if (!v1.equals(v2)) {
        return false;
      }
    }

    return true;
  }
}
