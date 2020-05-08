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

import com.github.tnakamot.json.token.JSONToken;
import org.jetbrains.annotations.NotNull;

/**
 * Represents one JSON 'object' value (immutable).
 *
 * <p>Instances of this class are immutable. Any method call that may result in the modification of
 * the object (e.g. {@link #put(JSONValueString, JSONValue)} results in {@link
 * UnsupportedOperationException}.
 *
 * <p>This implementation retains the order.
 *
 * @see JSONValueObjectMutable
 */
public class JSONValueObjectImmutable extends JSONValueObject {
  private final LinkedHashMap<JSONValueString, JSONValue> members;

  /**
   * Create an instance of a Java representation of a JSON 'object' value.
   *
   * @param members name/value pairs. Null is considered as an empty object.
   */
  public JSONValueObjectImmutable(Map<JSONValueString, JSONValue> members) {
    if (members == null) {
      this.members = new LinkedHashMap<>();
    } else {
      this.members = new LinkedHashMap<>(members);
    }
  }

  /** {@inheritDoc} */
  public JSONValue get(JSONValueString name) {
    return members.get(name);
  }

  /** {@inheritDoc} */
  public JSONValue get(String name) {
    return members.get(new JSONValueString(name));
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return members.size();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmpty() {
    return members.isEmpty();
  }

  /** {@inheritDoc} */
  @Override
  public boolean containsKey(Object o) {
    return members.containsKey(o);
  }

  /** {@inheritDoc} */
  public boolean containsKey(String name) {
    return members.containsKey(new JSONValueString(name));
  }

  /** {@inheritDoc} */
  @Override
  public boolean containsValue(Object o) {
    return members.containsValue(o);
  }

  /** {@inheritDoc} */
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

  /** {@inheritDoc} */
  @Deprecated
  @Override
  public JSONValue put(JSONValueString jsonValueString, JSONValue jsonValue) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Deprecated
  @Override
  public JSONValue remove(Object o) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Deprecated
  @Override
  public void putAll(@NotNull Map<? extends JSONValueString, ? extends JSONValue> map) {
    throw new UnsupportedOperationException("cannot put values");
  }

  /** {@inheritDoc} */
  @Deprecated
  @Override
  public void clear() {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public Set<JSONValueString> keySet() {
    return members.keySet();
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public Collection<JSONValue> values() {
    return members.values();
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public Set<Entry<JSONValueString, JSONValue>> entrySet() {
    return members.entrySet();
  }

  /**
   * Return the copy of this JSON object as a mutable Java object.
   *
   * <p>All inner JSON objects and JSON arrays are also turned to be mutable.
   *
   * @return a mutable version of the same JSON object.
   */
  public JSONValueObjectMutable toMutable() {
    JSONValueObjectMutable ret = new JSONValueObjectMutable();

    for (Map.Entry<JSONValueString, JSONValue> entry : entrySet()) {
      JSONValue value = entry.getValue();
      if (value instanceof JSONValueArrayImmutable) {
        ret.put(entry.getKey(), ((JSONValueArrayImmutable) value).toMutable());
      } else if (value instanceof JSONValueObjectImmutable) {
        ret.put(entry.getKey(), ((JSONValueObjectImmutable) value).toMutable());
      } else {
        ret.put(entry.getKey(), value);
      }
    }

    return ret;
  }

  @Override
  @NotNull
  public String toTokenString() {
    if (size() == 0) {
      return JSONToken.JSON_BEGIN_OBJECT + JSONToken.JSON_END_OBJECT;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(JSONToken.JSON_BEGIN_OBJECT);
    for (Map.Entry<JSONValueString, JSONValue> entry : this.entrySet()) {
      sb.append(entry.getKey().toTokenString());
      sb.append(JSONToken.JSON_NAME_SEPARATOR);
      sb.append(entry.getValue().toTokenString());
      sb.append(JSONToken.JSON_VALUE_SEPARATOR);
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append(JSONToken.JSON_END_OBJECT);
    return sb.toString();
  }

  @Override
  @NotNull
  public String toTokenString(String newline, String indent) {
    validateNewline(newline);
    validateIndent(indent);

    if (size() == 0) {
      return JSONToken.JSON_BEGIN_OBJECT + " " + JSONToken.JSON_END_OBJECT;
    }

    StringBuilder sb = new StringBuilder();
    sb.append(JSONToken.JSON_BEGIN_OBJECT);

    for (Map.Entry<JSONValueString, JSONValue> entry : this.entrySet()) {
      sb.append(newline);
      sb.append(indent);
      sb.append(entry.getKey().toTokenString(newline, indent));
      sb.append(JSONToken.JSON_NAME_SEPARATOR);
      sb.append(" ");

      String value = entry.getValue().toTokenString(newline, indent);
      String[] lines = value.split(newline);
      sb.append(lines[0]);
      for (int i = 1; i < lines.length; i++) {
        sb.append(newline);
        sb.append(indent);
        sb.append(lines[i]);
      }

      sb.append(JSONToken.JSON_VALUE_SEPARATOR);
    }
    sb.deleteCharAt(sb.length() - 1);

    sb.append(newline);
    sb.append(JSONToken.JSON_END_OBJECT);
    return sb.toString();
  }
}
