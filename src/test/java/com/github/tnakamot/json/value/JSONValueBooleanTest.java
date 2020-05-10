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

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueBooleanTest {
  @Test
  public void testTrue1() {
    JSONValueBoolean value = JSONValueBoolean.valueOf(true);
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertTrue(value.value());
    assertEquals("true", value.toString());
  }

  @Test
  public void testFalse1() {
    JSONValueBoolean value = JSONValueBoolean.valueOf(false);
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertFalse(value.value());
    assertEquals("false", value.toString());
  }

  @Test
  public void testTrue2() {
    JSONValueBoolean value = JSONValueBoolean.TRUE;
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertTrue(value.value());
    assertEquals("true", value.toString());
  }

  @Test
  public void testFalse2() {
    JSONValueBoolean value = JSONValueBoolean.FALSE;
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertFalse(value.value());
    assertEquals("false", value.toString());
  }

  @Test
  public void testTrue3() throws IOException, JSONParserException {
    JSONValueBoolean value = (JSONValueBoolean) JSONText.fromString("true").parse();
    assertNotNull(value);
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertTrue(value.value());
    assertEquals("true", value.toString());
  }

  @Test
  public void testFalse3() throws IOException, JSONParserException {
    JSONValueBoolean value = (JSONValueBoolean) JSONText.fromString("false").parse();
    assertNotNull(value);
    assertEquals(JSONValueType.BOOLEAN, value.type());
    assertFalse(value.value());
    assertEquals("false", value.toString());
  }

  @Test
  public void testTrueEquality() throws IOException, JSONParserException {
    JSONValueBoolean trueVal = JSONValueBoolean.valueOf(true);

    assertEquals(trueVal.hashCode(), trueVal.hashCode());
    assertEquals(trueVal, trueVal);
    assertEquals(JSONValueBoolean.TRUE.hashCode(), JSONValueBoolean.TRUE.hashCode());
    assertEquals(JSONValueBoolean.TRUE, JSONValueBoolean.TRUE);

    assertEquals(trueVal.hashCode(), JSONValueBoolean.TRUE.hashCode());
    assertEquals(trueVal, JSONValueBoolean.TRUE);
    assertEquals(JSONValueBoolean.TRUE, trueVal);

    JSONValue root = JSONText.fromString("[true, true, true]").parse();
    JSONValueArray array = (JSONValueArray) root;
    assertNotNull(array);
    for (JSONValue value : array) {
      assertTrue(value instanceof JSONValueBoolean);
      JSONValueBoolean boolVal = (JSONValueBoolean) value;
      assertEquals(trueVal.hashCode(), boolVal.hashCode());
      assertEquals(trueVal, boolVal);
      assertEquals(boolVal, trueVal);
    }
  }

  @Test
  public void testFalseEquality() throws IOException, JSONParserException {
    JSONValueBoolean falseVal = JSONValueBoolean.valueOf(false);

    assertEquals(falseVal.hashCode(), falseVal.hashCode());
    assertEquals(falseVal, falseVal);
    assertEquals(JSONValueBoolean.FALSE.hashCode(), JSONValueBoolean.FALSE.hashCode());
    assertEquals(JSONValueBoolean.FALSE, JSONValueBoolean.FALSE);

    assertEquals(falseVal.hashCode(), JSONValueBoolean.FALSE.hashCode());
    assertEquals(falseVal, JSONValueBoolean.FALSE);
    assertEquals(JSONValueBoolean.FALSE, falseVal);

    JSONValue root = JSONText.fromString("[false,false,false]").parse();
    JSONValueArray array = (JSONValueArray) root;
    assertNotNull(array);
    for (JSONValue value : array) {
      assertTrue(value instanceof JSONValueBoolean);
      JSONValueBoolean boolVal = (JSONValueBoolean) value;
      assertEquals(falseVal.hashCode(), boolVal.hashCode());
      assertEquals(falseVal, boolVal);
      assertEquals(boolVal, falseVal);
    }
  }

  @Test
  public void testInequality1() {
    JSONValueBoolean trueVal1 = JSONValueBoolean.valueOf(true);
    JSONValueBoolean falseVal1 = JSONValueBoolean.valueOf(false);

    JSONValueBoolean trueVal2 = JSONValueBoolean.TRUE;
    JSONValueBoolean falseVal2 = JSONValueBoolean.FALSE;

    assertNotEquals(trueVal1, falseVal1);
    assertNotEquals(falseVal1, trueVal1);

    assertNotEquals(trueVal2, falseVal2);
    assertNotEquals(falseVal2, trueVal2);

    assertNotEquals(trueVal2, falseVal1);
    assertNotEquals(falseVal1, trueVal2);

    assertNotEquals(falseVal2, trueVal1);
    assertNotEquals(trueVal1, falseVal2);
  }

  @Test
  public void testInequality2() throws IOException, JSONParserException {
    JSONValueBoolean trueVal1 = JSONValueBoolean.valueOf(true);
    JSONValueBoolean falseVal1 = JSONValueBoolean.valueOf(false);

    JSONValueArray root = (JSONValueArray) JSONText.fromString("[true,false]").parse();
    assertNotNull(root);
    JSONValueBoolean trueVal2 = (JSONValueBoolean) root.get(0);
    JSONValueBoolean falseVal2 = (JSONValueBoolean) root.get(1);

    assertNotEquals(trueVal1, falseVal1);
    assertNotEquals(falseVal1, trueVal1);

    assertNotEquals(trueVal2, falseVal2);
    assertNotEquals(falseVal2, trueVal2);

    assertNotEquals(trueVal2, falseVal1);
    assertNotEquals(falseVal1, trueVal2);

    assertNotEquals(falseVal2, trueVal1);
    assertNotEquals(trueVal1, falseVal2);
  }

  @Test
  public void testInequality3() throws IOException, JSONParserException {
    JSONValueBoolean trueVal1 = JSONValueBoolean.TRUE;
    JSONValueBoolean falseVal1 = JSONValueBoolean.FALSE;

    JSONValueArray root = (JSONValueArray) JSONText.fromString("[true,false]").parse();
    assertNotNull(root);
    JSONValueBoolean trueVal2 = (JSONValueBoolean) root.get(0);
    JSONValueBoolean falseVal2 = (JSONValueBoolean) root.get(1);
    JSONValueString strVal = new JSONValueString("abc");
    JSONValueNumber numVal1 = new JSONValueNumber(0);
    JSONValueNumber numVal2 = new JSONValueNumber(3.15);
    JSONValueNull nullVal = JSONValueNull.INSTANCE;

    assertNotEquals(trueVal1, falseVal1);
    assertNotEquals(falseVal1, trueVal1);

    assertNotEquals(trueVal2, falseVal2);
    assertNotEquals(falseVal2, trueVal2);

    assertNotEquals(trueVal2, falseVal1);
    assertNotEquals(falseVal1, trueVal2);

    assertNotEquals(falseVal2, trueVal1);
    assertNotEquals(trueVal1, falseVal2);

    assertNotEquals(trueVal1, strVal);
    assertNotEquals(trueVal1, numVal1);
    assertNotEquals(trueVal1, numVal2);
    assertNotEquals(trueVal1, nullVal);
    assertNotEquals(falseVal1, strVal);
    assertNotEquals(falseVal1, numVal1);
    assertNotEquals(falseVal1, numVal2);
    assertNotEquals(falseVal1, nullVal);
  }
}
