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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueTest {
  @Test
  public void testJSONValueArray() {
    JSONValue value = new JSONValueArrayMutable(new ArrayList<>());
    assertEquals(JSONValueType.ARRAY, value.type());
  }

  @Test
  public void testJSONValueBoolean() {
    JSONValue value1 = JSONValueBoolean.valueOf(true);
    assertEquals(JSONValueType.BOOLEAN, value1.type());

    JSONValue value2 = JSONValueBoolean.valueOf(false);
    assertEquals(JSONValueType.BOOLEAN, value2.type());

    JSONValue value3 = JSONValueBoolean.TRUE;
    assertEquals(JSONValueType.BOOLEAN, value3.type());

    JSONValue value4 = JSONValueBoolean.FALSE;
    assertEquals(JSONValueType.BOOLEAN, value4.type());
  }

  @Test
  public void testJSONValueNull() {
    JSONValue value = JSONValueNull.INSTANCE;
    assertEquals(JSONValueType.NULL, value.type());
  }

  @Test
  public void testJSONValueNumber() {
    JSONValue value = new JSONValueNumber("1");
    assertEquals(JSONValueType.NUMBER, value.type());
  }

  @Test
  public void testJSONValueObject() {
    JSONValue value = new JSONValueObjectMutable(new HashMap<>());
    assertEquals(JSONValueType.OBJECT, value.type());
  }

  @Test
  public void testJSONValueString() {
    JSONValue value = new JSONValueString("abc");
    assertEquals(JSONValueType.STRING, value.type());
  }

  @Test
  public void testToTokenString() {
    JSONValueObject root = new JSONValueObjectMutable();
    root.put("key1", "test");
    root.put("key2", 3.15);

    assertThrows(IllegalArgumentException.class, () -> root.toTokenString("\n\r", "  "));
    assertThrows(IllegalArgumentException.class, () -> root.toTokenString("\n", "abc"));
  }
}
