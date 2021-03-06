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

import com.github.tnakamot.json.token.JSONToken;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/** Represents one JSON 'object' value. */
public abstract class JSONValueObject extends JSONValueStructured
    implements Map<JSONValueString, JSONValue> {
  /** Create an instance of a Java representation of a JSON 'object' value. */
  JSONValueObject() {
    super(JSONValueType.OBJECT);
  }

  /**
   * Create an instance of a Java representation of a JSON 'object' value.
   *
   * @param begin the beginning token of this JSON object. Null if this JSON object does not
   *     originate from an exsiting JSON text.
   * @param end the end token of this JSON object. Null if this JSON object does not originate from
   *     an exsiting JSON text.
   */
  JSONValueObject(JSONToken begin, JSONToken end) {
    super(JSONValueType.OBJECT, begin, end);
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

  /**
   * Returns the boolean value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not boolean
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public boolean getBoolean(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueBoolean) {
      return ((JSONValueBoolean) val).value();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.BOOLEAN, val.type());
    }
  }

  /**
   * Returns the long value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not number
   * @throws NumberFormatException if the value cannot be converted to a Java long value
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public long getLong(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueNumber) {
      return ((JSONValueNumber) val).toLong();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.NUMBER, val.type());
    }
  }

  /**
   * Returns the double value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not number
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public double getDouble(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueNumber) {
      return ((JSONValueNumber) val).toDouble();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.NUMBER, val.type());
    }
  }

  /**
   * Returns the string value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not string
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public String getString(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueString) {
      return ((JSONValueString) val).value();
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.STRING, val.type());
    }
  }

  /**
   * Returns the array value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not array
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public JSONValueArray getArray(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueArray) {
      return (JSONValueArray) val;
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.ARRAY, val.type());
    }
  }

  /**
   * Returns the object value to which the specified key is mapped, or null if this map contains no
   * mapping for the key.
   *
   * @param key the key whose associated value is to be returned
   * @return the value to which the specified key is mapped
   * @throws WrongValueTypeException if the value type is not object
   * @throws IllegalArgumentException if this object contains no mapping for the key
   */
  public JSONValueObject getObject(String key) {
    JSONValue val = get(key);
    if (val == null) {
      throw new IllegalArgumentException("Key '" + key + "' does not exist.");
    } else if (val instanceof JSONValueObject) {
      return (JSONValueObject) val;
    } else {
      throw new WrongValueTypeException(
          "Wrong value type for key '" + key + "'.", JSONValueType.OBJECT, val.type());
    }
  }

  @Override
  public int hashCode() {
    int hash = 0;
    for (Map.Entry<JSONValueString, JSONValue> entry : this.entrySet()) {
      hash += entry.getKey().hashCode();
      hash += entry.getValue().hashCode();
    }

    return hash;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof JSONValueObject)) {
      return false;
    }
    JSONValueObject obj = (JSONValueObject) o;
    if (size() != obj.size()) {
      return false;
    }

    for (Map.Entry<JSONValueString, JSONValue> entry : this.entrySet()) {
      if (!obj.get(entry.getKey()).equals(entry.getValue())) {
        return false;
      }
    }

    return true;
  }
}
