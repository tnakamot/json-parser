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
import com.github.tnakamot.json.token.JSONToken;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueStringTest {
  @Test
  public void testNullAndEmpty() {
    JSONValueString val1 = new JSONValueString((String) null);
    JSONValueString val2 = new JSONValueString("");

    assertEquals(JSONValueType.STRING, val1.type());
    assertEquals(JSONValueType.STRING, val2.type());

    assertEquals(val1.hashCode(), val2.hashCode());
    assertEquals(val1, val2);

    assertEquals("", val1.value());
    assertEquals("", val2.value());

    assertEquals("", val1.toString());
    assertEquals("", val2.toString());
  }

  @Test
  public void testType() throws IOException, JSONParserException {
    JSONValueString val1 = new JSONValueString("abc");
    JSONValueString val2 = (JSONValueString) JSONText.fromString("\"def\"").parse().root();
    assertNotNull(val2);

    assertEquals(JSONValueType.STRING, val1.type());
    assertEquals(JSONValueType.STRING, val2.type());
  }

  @Test
  public void testEquality() throws IOException, JSONParserException {
    JSONValueString val1 = new JSONValueString("abc");
    JSONValueString val2 = new JSONValueString("abc");
    JSONValueString val3 = (JSONValueString) JSONText.fromString("\"abc\"").parse().root();
    assertNotNull(val3);

    assertEquals(val1.hashCode(), val2.hashCode());
    assertEquals(val1.hashCode(), val3.hashCode());

    assertEquals(val1, val2);
    assertEquals(val2, val1);
    assertEquals(val1, val3);
    assertEquals(val3, val1);
    assertEquals(val2, val3);
    assertEquals(val3, val2);
  }

  @Test
  public void testInequality() throws IOException, JSONParserException {
    JSONValueString val1 = new JSONValueString("abc");
    JSONValueString val2 = new JSONValueString("xyz");
    JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse().root();
    assertNotNull(val3);

    assertNotEquals(val1.hashCode(), val2.hashCode());
    assertNotEquals(val2.hashCode(), val3.hashCode());
    assertNotEquals(val3.hashCode(), val1.hashCode());

    assertNotEquals(val1, val2);
    assertNotEquals(val2, val1);
    assertNotEquals(val1, val3);
    assertNotEquals(val3, val1);
    assertNotEquals(val2, val3);
    assertNotEquals(val3, val2);
  }

  @Test
  public void testValue() throws IOException, JSONParserException {
    JSONValueString val1 = new JSONValueString("abc");
    JSONValueString val2 = new JSONValueString("xyz");
    JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse().root();
    assertNotNull(val3);

    assertEquals("abc", val1.value());
    assertEquals("xyz", val2.value());
    assertEquals("hello", val3.value());
  }

  @Test
  public void testToString() throws IOException, JSONParserException {
    JSONValueString val1 = new JSONValueString("abc");
    JSONValueString val2 = new JSONValueString("xyz");
    JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse().root();
    assertNotNull(val3);

    assertEquals("abc", val1.toString());
    assertEquals("xyz", val2.toString());
    assertEquals("hello", val3.toString());
  }

  @Test
  public void testSource() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("{\"key1\": 1.23, \"key2\": \"te\\nst\" }").parse().root();
    JSONValueObject rootObj = (JSONValueObject) root;
    JSONValueString str = (JSONValueString) rootObj.get("key2");
    JSONToken token = str.token();
    System.out.println("Token: " + token.text());
    System.out.println(
        String.format(
            "Start: line %d, column %d",
            token.beginningLocation().line(), token.beginningLocation().column()));
    System.out.println(
        String.format(
            "End  : line %d, column %d", token.endLocation().line(), token.endLocation().column()));
  }
}
