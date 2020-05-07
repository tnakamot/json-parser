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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** Represents one JSON 'object' value. */
public abstract class JSONValueObject extends JSONValue implements Map<JSONValueString, JSONValue> {
  /** Create an instance of a Java representation of a JSON 'object' value. */
  JSONValueObject() {
    super(JSONValueType.OBJECT);
  }

  /**
   * Returns the JSON value of the given name.
   *
   * <p>If a value of the given name does not exist, this method returns null. Do not confuse it
   * with a JSON 'null' value. If there is a JSON 'null' value of the given name, this method
   * returns an instance of {@link JSONValueNull} instead.
   *
   * @param name a JSON string value which represents a name
   * @return a JSON value of the given name
   */
  public abstract JSONValue get(JSONValueString name);

  /**
   * Returns the JSON value which has the given name.
   *
   * @param name name
   * @return a JSON value of the given name
   * @see #get(JSONValueString)
   */
  public abstract JSONValue get(String name);

  /** {@inheritDoc} */
  @Override
  public abstract int size();

  /** {@inheritDoc} */
  @Override
  public abstract boolean isEmpty();

  /** {@inheritDoc} */
  @Override
  public abstract boolean containsKey(Object o);

  /**
   * Check if this JSON object has a value which has the given name.
   *
   * @param name name
   * @return true if a value with the given name exists
   * @see #containsKey(Object)
   */
  public abstract boolean containsKey(String name);

  /** {@inheritDoc} */
  @Override
  public abstract boolean containsValue(Object o);

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue get(Object o);

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue put(JSONValueString jsonValueString, JSONValue jsonValue);

  /** {@inheritDoc} */
  @Override
  public abstract JSONValue remove(Object o);

  public JSONValue remove(String key) {
    return remove(new JSONValueString(key));
  }

  /** {@inheritDoc} */
  @Override
  public abstract void putAll(@NotNull Map<? extends JSONValueString, ? extends JSONValue> map);

  /** {@inheritDoc} */
  @Override
  public abstract void clear();

  /** {@inheritDoc} */
  @Override
  public abstract @NotNull Set<JSONValueString> keySet();

  /** {@inheritDoc} */
  @Override
  public abstract @NotNull Collection<JSONValue> values();

  /** {@inheritDoc} */
  @Override
  public abstract @NotNull Set<Entry<JSONValueString, JSONValue>> entrySet();

  /** {@inheritDoc} */
  @Override
  public abstract int hashCode();

  /** {@inheritDoc} */
  @Override
  public abstract boolean equals(Object o);

  /**
   * Associates the specified value with the specified key in this JSON object.
   *
   * @param key key with which the specified value is to be associated
   * @param value value to be associated with the specified key
   * @return the previous value associated with key, or null if there was no mapping for key. (A
   *     null return can also indicate that the map previously associated null with key, if the
   *     implementation supports null values.)
   */
  public JSONValue put(String key, JSONValue value) {
    return put(new JSONValueString(key), value);
  }

  /**
   * Associates the specified boolean value with the specified key in this JSON object.
   *
   * @param key key with which the specified value is to be associated
   * @param value booolean value to be associated with the specified key
   * @return the previous value associated with key, or null if there was no mapping for key. (A
   *     null return can also indicate that the map previously associated null with key, if the
   *     implementation supports null values.)
   */
  public JSONValue put(String key, boolean value) {
    return put(key, JSONValueBoolean.valueOf(value));
  }

  /**
   * Associates the specified number value with the specified key in this JSON object.
   *
   * @param key key with which the specified value is to be associated
   * @param value number value to be associated with the specified key
   * @return the previous value associated with key, or null if there was no mapping for key. (A
   *     null return can also indicate that the map previously associated null with key, if the
   *     implementation supports null values.)
   */
  public JSONValue put(String key, long value) {
    return put(key, new JSONValueNumber(value));
  }

  /**
   * Associates the specified number value with the specified key in this JSON object.
   *
   * @param key key with which the specified value is to be associated
   * @param value number value to be associated with the specified key
   * @return the previous value associated with key, or null if there was no mapping for key. (A
   *     null return can also indicate that the map previously associated null with key, if the
   *     implementation supports null values.)
   */
  public JSONValue put(String key, double value) {
    return put(key, new JSONValueNumber(value));
  }

  /**
   * Associates the specified string value with the specified key in this JSON object.
   *
   * @param key key with which the specified value is to be associated
   * @param value string alue to be associated with the specified key. Null is considered as an
   *     empty string.
   * @return the previous value associated with key, or null if there was no mapping for key. (A
   *     null return can also indicate that the map previously associated null with key, if the
   *     implementation supports null values.)
   */
  public JSONValue put(String key, String value) {
    return put(key, new JSONValueString(value));
  }
}
