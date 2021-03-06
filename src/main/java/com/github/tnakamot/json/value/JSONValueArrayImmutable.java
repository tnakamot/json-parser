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

import com.github.tnakamot.json.token.JSONToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one JSON 'array' value (immutable).
 *
 * <p>Instances of this class are immutable. Any method call that may result in the modification of
 * the array (e.g. {@link #add(JSONValue)} results in {@link UnsupportedOperationException}.
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
  JSONValueArrayImmutable(@Nullable List<JSONValue> values) {
    this(values, null, null);
  }

  /**
   * Create an instance of a Java representation of a JSON 'array' value.
   *
   * @param values sequence of values. Null is considered as an empty array.
   * @param begin the beginning token of this JSON array. Null if this JSON array does not originate
   *     from an exsiting JSON text.
   * @param end the end token of this JSON array. Null if this JSON array does not originate from an
   *     exsiting JSON text.
   */
  JSONValueArrayImmutable(
      @Nullable List<JSONValue> values, @Nullable JSONToken begin, @Nullable JSONToken end) {
    super(begin, end);

    if (values == null) {
      this.values = new ArrayList<>();
    } else {
      this.values = new ArrayList<>(values);
    }
  }

  /** {@inheritDoc} */
  @Override
  public int size() {
    return values.size();
  }

  /** {@inheritDoc} */
  @Override
  public boolean isEmpty() {
    return values.isEmpty();
  }

  /** {@inheritDoc} */
  @Override
  public boolean contains(Object o) {
    return values.contains(o);
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public Iterator<JSONValue> iterator() {
    return values.iterator();
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public Object[] toArray() {
    return values.toArray();
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public <T> T[] toArray(T[] ts) {
    return values.toArray(ts);
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean add(JSONValue jsonValue) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("cannot remove a value");
  }

  /** {@inheritDoc} */
  @Override
  public boolean containsAll(@NotNull Collection collection) {
    return values.containsAll(collection);
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean addAll(@NotNull Collection collection) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean addAll(int i, @NotNull Collection collection) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public void clear() {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  public JSONValue get(int i) {
    return values.get(i);
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public JSONValue set(int i, JSONValue jsonValue) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public void add(int i, JSONValue jsonValue) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public JSONValue remove(int i) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  public int indexOf(Object o) {
    return values.indexOf(o);
  }

  /** {@inheritDoc} */
  @Override
  public int lastIndexOf(Object o) {
    return values.lastIndexOf(o);
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public ListIterator<JSONValue> listIterator() {
    return values.listIterator();
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public ListIterator<JSONValue> listIterator(int i) {
    return values.listIterator(i);
  }

  /** {@inheritDoc} */
  @Override
  @NotNull
  public List<JSONValue> subList(int i, int i1) {
    return values.subList(i, i1);
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean retainAll(@NotNull Collection<?> collection) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /** {@inheritDoc} */
  @Override
  @Deprecated
  public boolean removeAll(@NotNull Collection<?> collection) {
    throw new UnsupportedOperationException("this object is immutable.");
  }

  /**
   * Return the copy of this JSON array as a mutable Java object.
   *
   * <p>All inner JSON objects and JSON arrays are also turned to be mutable.
   *
   * @return a mutable version of the same JSON array.
   */
  public JSONValueArrayMutable toMutable() {
    JSONValueArrayMutable ret = new JSONValueArrayMutable();
    for (JSONValue value : this) {
      if (value instanceof JSONValueArrayImmutable) {
        ret.add(((JSONValueArrayImmutable) value).toMutable());
      } else if (value instanceof JSONValueObjectImmutable) {
        ret.add(((JSONValueObjectImmutable) value).toMutable());
      } else {
        ret.add(value);
      }
    }

    return ret;
  }

  @Override
  @NotNull
  public String toTokenString() {
    if (size() == 0) {
      return JSONToken.JSON_BEGIN_ARRAY + JSONToken.JSON_END_ARRAY;
    }

    return toTokenStringSingleLine(false);
  }

  @Override
  @NotNull
  public String toTokenString(String newline, String indent) {
    if (size() == 0) {
      return JSONToken.JSON_BEGIN_ARRAY + " " + JSONToken.JSON_END_ARRAY;
    }

    boolean multiLine =
        this.stream()
            .anyMatch(
                (value) ->
                    value.type() == JSONValueType.OBJECT || value.type() == JSONValueType.ARRAY);

    if (multiLine) {
      return toTokenStringMultiLine(newline, indent);
    }

    String singleLine = toTokenStringSingleLine(true);
    if (singleLine.length() <= 80) { // TODO: make this limit configurable
      return singleLine;
    } else {
      return toTokenStringMultiLine(newline, indent);
    }
  }

  private String toTokenStringSingleLine(boolean wsAfterSeparator) {
    StringBuilder sb = new StringBuilder();
    sb.append(JSONToken.JSON_BEGIN_ARRAY);

    for (JSONValue value : this) {
      sb.append(value.toTokenString());
      sb.append(JSONToken.JSON_VALUE_SEPARATOR);
      if (wsAfterSeparator) {
        sb.append(" ");
      }
    }

    if (wsAfterSeparator) {
      sb.deleteCharAt(sb.length() - 1);
    }
    sb.deleteCharAt(sb.length() - 1);

    sb.append(JSONToken.JSON_END_ARRAY);
    return sb.toString();
  }

  private String toTokenStringMultiLine(String newline, String indent) {
    validateNewline(newline);
    validateIndent(indent);

    StringBuilder sb = new StringBuilder();
    sb.append(JSONToken.JSON_BEGIN_ARRAY);

    for (JSONValue value : this) {
      sb.append(newline);
      sb.append(indent);

      String[] lines = value.toTokenString(newline, indent).split(newline);
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
    sb.append(JSONToken.JSON_END_ARRAY);
    return sb.toString();
  }
}
