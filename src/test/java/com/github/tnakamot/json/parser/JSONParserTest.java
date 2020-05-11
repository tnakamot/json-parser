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

package com.github.tnakamot.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.token.JSONToken;
import com.github.tnakamot.json.token.StringLocation;
import com.github.tnakamot.json.value.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONParserTest {
  private static final Logger log = LoggerFactory.getLogger(JSONParserTest.class);

  @Test
  public void testEmpty() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("").parse().root();
    assertNull(root);
  }

  @Test
  public void testWSOnly() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" \r\n\t").parse().root();
    assertNull(root);
  }

  @Test
  public void testEmptyObject() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" { } ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.OBJECT, root.type());
    assertTrue(root instanceof JSONValueObject);

    JSONValueObject rootObj = (JSONValueObject) root;

    assertEquals(0, rootObj.size());
    assertFalse(rootObj.containsKey("key"));
    assertTrue(rootObj.isEmpty());
  }

  @Test
  public void testEmptyArray() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("[]").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.ARRAY, root.type());
    assertTrue(root instanceof JSONValueArray);

    JSONValueArray rootArray = (JSONValueArray) root;

    assertEquals(0, rootArray.size());
    assertFalse(rootArray.contains(JSONValueNull.INSTANCE));
    assertTrue(rootArray.isEmpty());
  }

  @Test
  public void testNullOnly() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("null").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NULL, root.type());
    assertTrue(root instanceof JSONValueNull);
    assertEquals(JSONValueNull.INSTANCE, root);
    assertEquals("null", root.toString());
  }

  @Test
  public void testTrueOnly() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" true ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.BOOLEAN, root.type());
    assertTrue(root instanceof JSONValueBoolean);
    assertEquals(JSONValueBoolean.TRUE, root);
    assertEquals("true", root.toString());

    JSONValueBoolean bool = (JSONValueBoolean) root;
    assertTrue(bool.value());
  }

  @Test
  public void testFalseOnly() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("false").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.BOOLEAN, root.type());
    assertTrue(root instanceof JSONValueBoolean);
    assertEquals(JSONValueBoolean.FALSE, root);
    assertEquals("false", root.toString());

    JSONValueBoolean bool = (JSONValueBoolean) root;
    assertFalse(bool.value());
  }

  @Test
  public void testStringOnly() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" \"abc\"").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.STRING, root.type());
    assertTrue(root instanceof JSONValueString);
    assertEquals(new JSONValueString("abc"), root);
    assertEquals("abc", root.toString());

    JSONValueString str = (JSONValueString) root;
    assertEquals("abc", str.value());
  }

  @Test
  public void testNumberOnly00() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" -15.234e2 ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("-15.234e2"), root);
    assertEquals("-15.234e2", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(-15.234e2, num.toDouble());
  }

  @Test
  public void testNumberOnly01() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("523 ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("523"), root);
    assertEquals("523", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertTrue(num.canBeLong());
    assertEquals(523, num.toLong());
  }

  @Test
  public void testNumberOnly02() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("-124 ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("-124"), root);
    assertEquals("-124", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertTrue(num.canBeLong());
    assertEquals(-124, num.toLong());
  }

  @Test
  public void testNumberOnly03() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" 928.5").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("928.5"), root);
    assertEquals("928.5", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(928.5, num.toDouble());
  }

  @Test
  public void testNumberOnly04() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" -872.512").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("-872.512"), root);
    assertEquals("-872.512", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(-872.512, num.toDouble());
  }

  @Test
  public void testNumberOnly05() {
    // Because leading zero is not allowed, this text is considered
    // to have two number tokens "0" and "12". Two numbers in a row
    // are not valid in JSON.
    JSONText jsText = JSONText.fromString(" 012 ");
    JSONParserErrorHandlingOptions opt =
        JSONParserErrorHandlingOptions.builder().showErrorLine(true).build();

    JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(opt));
    assertEquals(jsText, ex.source());
    assertEquals(2, ex.location().beginning().position());
    assertEquals(1, ex.location().beginning().line());
    assertEquals(3, ex.location().beginning().column());
    assertEquals(3, ex.location().end().position());
    assertEquals(1, ex.location().end().line());
    assertEquals(4, ex.location().end().column());
    log.info(ex::getMessage);
  }

  @Test
  public void testNumberOnly06() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString(" -0.015   ").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("-0.015"), root);
    assertEquals("-0.015", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(-0.015, num.toDouble());
  }

  @Test
  public void testNumberOnly07() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("   0.987").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("0.987"), root);
    assertEquals("0.987", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(0.987, num.toDouble());
  }

  @Test
  public void testNumberOnly08() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("1e6").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("1e6"), root);
    assertEquals("1e6", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertTrue(num.canBeLong());
    assertEquals(1000000, num.toLong());
    assertEquals(1e6, num.toDouble());
  }

  @Test
  public void testNumberOnly09() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("1.24e-12").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("1.24e-12"), root);
    assertEquals("1.24e-12", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertFalse(num.canBeLong());
    assertEquals(1.24e-12, num.toDouble());
  }

  @Test
  public void testNumberOnly10() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("-5.2E+2").parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.NUMBER, root.type());
    assertTrue(root instanceof JSONValueNumber);
    assertEquals(new JSONValueNumber("-5.2E+2"), root);
    assertEquals("-5.2E+2", root.toString());

    JSONValueNumber num = (JSONValueNumber) root;
    assertTrue(num.canBeLong());
    assertEquals(-520, num.toLong());
    assertEquals(-5.2e2, num.toDouble());
  }

  @Test
  public void testSimpleArray() throws IOException, JSONParserException {
    JSONText jsText = JSONText.fromString(" [ true, false, \"abc\", 1.52, null ] ");
    JSONValue root = jsText.parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.ARRAY, root.type());
    assertTrue(root instanceof JSONValueArray);

    JSONValueArray rootArray = (JSONValueArray) root;
    assertFalse(rootArray.isEmpty());
    assertEquals(5, rootArray.size());

    assertEquals(JSONValueBoolean.valueOf(true), rootArray.get(0));
    assertTrue(rootArray.contains(JSONValueBoolean.valueOf(true)));
    assertEquals(JSONValueType.BOOLEAN, rootArray.get(0).type());
    assertTrue(rootArray.get(0) instanceof JSONValueBoolean);
    assertTrue(((JSONValueBoolean) rootArray.get(0)).value());

    assertEquals(JSONValueBoolean.valueOf(false), rootArray.get(1));
    assertTrue(rootArray.contains(JSONValueBoolean.valueOf(false)));
    assertEquals(JSONValueType.BOOLEAN, rootArray.get(1).type());
    assertTrue(rootArray.get(1) instanceof JSONValueBoolean);
    assertFalse(((JSONValueBoolean) rootArray.get(1)).value());

    assertEquals(new JSONValueString("abc"), rootArray.get(2));
    assertTrue(rootArray.contains(new JSONValueString("abc")));
    assertEquals(JSONValueType.STRING, rootArray.get(2).type());
    assertTrue(rootArray.get(2) instanceof JSONValueString);
    assertEquals("abc", ((JSONValueString) rootArray.get(2)).value());

    assertEquals(new JSONValueNumber("1.52"), rootArray.get(3));
    assertTrue(rootArray.contains(new JSONValueNumber("1.52")));
    assertEquals(JSONValueType.NUMBER, rootArray.get(3).type());
    assertTrue(rootArray.get(3) instanceof JSONValueNumber);
    assertEquals("1.52", rootArray.get(3).toString());
    assertEquals(1.52, ((JSONValueNumber) rootArray.get(3)).toDouble());
    assertFalse(((JSONValueNumber) rootArray.get(3)).canBeLong());

    assertEquals(JSONValueNull.INSTANCE, rootArray.get(4));
    assertTrue(rootArray.contains(JSONValueNull.INSTANCE));
    assertEquals(JSONValueType.NULL, rootArray.get(4).type());
    assertTrue(rootArray.get(4) instanceof JSONValueNull);

    int i = 0;
    for (JSONValue jsonValue : rootArray) {
      switch (i) {
        case 0:
          assertEquals(JSONValueType.BOOLEAN, jsonValue.type());
          assertEquals(JSONValueBoolean.TRUE, jsonValue);
          break;
        case 1:
          assertEquals(JSONValueType.BOOLEAN, jsonValue.type());
          assertEquals(JSONValueBoolean.FALSE, jsonValue);
          break;
        case 2:
          assertEquals(JSONValueType.STRING, jsonValue.type());
          assertEquals(new JSONValueString("abc"), jsonValue);
          break;
        case 3:
          assertEquals(JSONValueType.NUMBER, jsonValue.type());
          assertEquals(new JSONValueNumber("1.52"), jsonValue);
          break;
        case 4:
          assertEquals(JSONValueType.NULL, jsonValue.type());
          assertEquals(JSONValueNull.INSTANCE, jsonValue);
          break;
        default:
          throw new UnsupportedOperationException("the code must not reach here.");
      }

      i++;
    }
  }

  @Test
  public void testSimpleObject() throws IOException, JSONParserException {
    JSONText jsText =
        JSONText.fromString(" { \"key1\": true, " + "   \"key2\": false," + "   \"key3\": null } ");
    JSONValue root = jsText.parse().root();

    assertNotNull(root);
    assertEquals(JSONValueType.OBJECT, root.type());
    assertTrue(root instanceof JSONValueObject);

    JSONValueObject rootObj = (JSONValueObject) root;

    assertEquals(3, rootObj.size());
    assertFalse(rootObj.isEmpty());
    assertTrue(rootObj.containsKey("key1"));
    assertTrue(rootObj.containsKey("key2"));
    assertTrue(rootObj.containsKey("key3"));
    assertFalse(rootObj.containsKey("key4"));

    assertEquals(JSONValueBoolean.valueOf(true), rootObj.get("key1"));
    assertTrue(rootObj.containsValue(JSONValueBoolean.TRUE));
    assertEquals(JSONValueType.BOOLEAN, rootObj.get("key1").type());
    assertTrue(rootObj.get("key1") instanceof JSONValueBoolean);
    assertTrue(((JSONValueBoolean) rootObj.get("key1")).value());

    assertEquals(JSONValueBoolean.FALSE, rootObj.get("key2"));
    assertTrue(rootObj.containsValue(JSONValueBoolean.valueOf(false)));
    assertEquals(JSONValueType.BOOLEAN, rootObj.get("key2").type());
    assertTrue(rootObj.get("key2") instanceof JSONValueBoolean);
    assertFalse(((JSONValueBoolean) rootObj.get("key2")).value());

    assertEquals(JSONValueNull.INSTANCE, rootObj.get("key3"));
    assertTrue(rootObj.containsValue(JSONValueNull.INSTANCE));
    assertEquals(JSONValueType.NULL, rootObj.get("key3").type());
    assertTrue(rootObj.get("key3") instanceof JSONValueNull);

    assertNull(rootObj.get("key4"));

    int i = 0;
    for (Map.Entry<JSONValueString, JSONValue> entry : rootObj.entrySet()) {
      switch (i) {
        case 0:
          assertEquals("key1", entry.getKey().value());
          assertEquals(JSONValueBoolean.TRUE, entry.getValue());
          break;
        case 1:
          assertEquals("key2", entry.getKey().value());
          assertEquals(JSONValueBoolean.FALSE, entry.getValue());
          break;
        case 2:
          assertEquals("key3", entry.getKey().value());
          assertEquals(JSONValueNull.INSTANCE, entry.getValue());
          break;
        default:
          throw new UnsupportedOperationException("the code must not reach here.");
      }
      i++;
    }
  }

  @Test
  public void testImmutable1() throws IOException, JSONParserException, URISyntaxException {
    URL example =
        this.getClass().getResource("/com/github/tnakamot/json/rfc8259/rfc8259_example1.json");
    JSONText jsText = JSONText.fromURL(example);
    JSONValue root = jsText.parse().root();

    assertTrue(root instanceof JSONValueObjectImmutable);
    JSONValueObject rootObj = (JSONValueObject) root;
    UnsupportedOperationException ex1 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> rootObj.put(new JSONValueString("test"), new JSONValueString("value")));
    log.info(ex1, ex1::getMessage);

    JSONValue image = rootObj.get("Image");
    assertTrue(image instanceof JSONValueObjectImmutable);
    JSONValueObject imageObj = (JSONValueObject) image;
    UnsupportedOperationException ex2 =
        assertThrows(UnsupportedOperationException.class, imageObj::clear);
    log.info(ex2, ex2::getMessage);

    JSONValue thumbnail = imageObj.get("Thumbnail");
    assertTrue(thumbnail instanceof JSONValueObjectImmutable);
    JSONValueObject thumbnailObj = (JSONValueObject) thumbnail;
    UnsupportedOperationException ex3 =
        assertThrows(UnsupportedOperationException.class, () -> thumbnailObj.remove("Url"));
    log.info(ex3, ex3::getMessage);

    JSONValue ids = imageObj.get("IDs");
    assertTrue(ids instanceof JSONValueArrayImmutable);
    JSONValueArray idsArray = (JSONValueArray) ids;
    UnsupportedOperationException ex4 =
        assertThrows(
            UnsupportedOperationException.class, () -> idsArray.add(new JSONValueString("value")));
    log.info(ex4, ex4::getMessage);
  }

  @Test
  public void testImmutable2() throws IOException, JSONParserException, URISyntaxException {
    URL example =
        this.getClass().getResource("/com/github/tnakamot/json/rfc8259/rfc8259_example2.json");
    JSONText jsText = JSONText.fromURL(example);
    JSONValue root = jsText.parse().root();

    assertTrue(root instanceof JSONValueArrayImmutable);
    JSONValueArray rootArray = (JSONValueArray) root;
    UnsupportedOperationException ex1 =
        assertThrows(
            UnsupportedOperationException.class, () -> rootArray.add(new JSONValueString("test")));
    log.info(ex1, ex1::getMessage);

    JSONValue element1 = rootArray.get(0);
    assertTrue(element1 instanceof JSONValueObjectImmutable);
    JSONValueObject element1Obj = (JSONValueObject) element1;
    UnsupportedOperationException ex2 =
        assertThrows(UnsupportedOperationException.class, element1Obj::clear);
    log.info(ex2, ex2::getMessage);

    JSONValue element2 = rootArray.get(1);
    assertTrue(element2 instanceof JSONValueObjectImmutable);
    JSONValueObject element2Obj = (JSONValueObject) element2;
    UnsupportedOperationException ex3 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> element2Obj.remove(new JSONValueString("Zip")));
    log.info(ex3, ex3::getMessage);
  }

  @Test
  public void testMutable1() throws IOException, JSONParserException, URISyntaxException {
    URL example =
        this.getClass().getResource("/com/github/tnakamot/json/rfc8259/rfc8259_example1.json");
    JSONText jsText = JSONText.fromURL(example);
    JSONValue rootImmutable = jsText.parse().root();
    assertTrue(rootImmutable instanceof JSONValueObjectImmutable);

    JSONValueObject rootObj = ((JSONValueObjectImmutable) rootImmutable).toMutable();
    rootObj.put(new JSONValueString("test"), new JSONValueString("value"));
    assertEquals(new JSONValueString("value"), rootObj.get("test"));

    JSONValue image = rootObj.get("Image");
    assertTrue(image instanceof JSONValueObjectMutable);
    JSONValueObject imageObj = (JSONValueObject) image;
    imageObj.put("newKey", new JSONValueString("hello"));
    assertEquals(new JSONValueString("hello"), imageObj.get("newKey"));

    JSONValue thumbnail = imageObj.get("Thumbnail");
    assertTrue(thumbnail instanceof JSONValueObjectMutable);
    JSONValueObject thumbnailObj = (JSONValueObject) thumbnail;
    thumbnailObj.clear();
    assertEquals(0, thumbnailObj.size());

    JSONValue ids = imageObj.get("IDs");
    assertTrue(ids instanceof JSONValueArrayMutable);
    JSONValueArray idsArray = (JSONValueArray) ids;
    idsArray.add(new JSONValueNumber("6123"));
    assertEquals(new JSONValueNumber("6123"), idsArray.get(idsArray.size() - 1));
  }

  @Test
  public void testMutable2() throws IOException, JSONParserException, URISyntaxException {
    URL example =
        this.getClass().getResource("/com/github/tnakamot/json/rfc8259/rfc8259_example2.json");
    JSONText jsText = JSONText.fromURL(example);
    JSONValue root = jsText.parse().root();
    assertTrue(root instanceof JSONValueArrayImmutable);

    JSONValueArray rootArray = ((JSONValueArrayImmutable) root).toMutable();
    int originalSize = rootArray.size();
    rootArray.add(new JSONValueString("test"));
    assertEquals(originalSize + 1, rootArray.size());

    JSONValue element1 = rootArray.get(0);
    assertTrue(element1 instanceof JSONValueObjectMutable);
    JSONValueObject element1Obj = (JSONValueObject) element1;
    element1Obj.clear();
    assertEquals(0, element1Obj.size());

    JSONValue element2 = rootArray.get(1);
    assertTrue(element2 instanceof JSONValueObjectMutable);
    JSONValueObject element2Obj = (JSONValueObject) element2;
    assertTrue(element2Obj.containsKey("Zip"));
    element2Obj.remove("Zip");
    assertFalse(element2Obj.containsKey("Zip"));
  }

  @Test
  public void testDuplicateKeyFail() {
    JSONParserErrorHandlingOptions opt =
        JSONParserErrorHandlingOptions.builder()
            .showErrorLine(true)
            .failOnDuplicateKey(true)
            .build();

    JSONText jsText = JSONText.fromString("{\"key1\": true, \"key2\": false, \"key1\": null}");

    JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(opt));
    log.info(ex::getMessage);
  }

  @Test
  public void testDuplicateKeyIgnore() throws IOException, JSONParserException {
    JSONParserErrorHandlingOptions opt =
        JSONParserErrorHandlingOptions.builder().failOnDuplicateKey(false).build();

    JSONText jsText =
        JSONText.fromString(
            "{\"key1\": true, \"key2\": false, \"key1\": null, \"key1\": false, \"key2\": null}");
    String[] lines = jsText.get().split("\r|(\r?\n)");

    JSONParserResult result = jsText.parse(opt);
    assertEquals(2, result.duplicateKeys().size());
    assertEquals(3, result.duplicateKeys().get(0).size());
    assertEquals("key1", result.duplicateKeys().get(0).get(0).value());
    assertEquals(1, result.duplicateKeys().get(0).get(0).token().range().beginning().line());
    assertEquals(2, result.duplicateKeys().get(0).get(0).token().range().beginning().column());
    assertEquals(1, result.duplicateKeys().get(0).get(0).token().range().end().line());
    assertEquals(7, result.duplicateKeys().get(0).get(0).token().range().end().column());
    assertEquals("key1", result.duplicateKeys().get(0).get(1).value());
    assertEquals(1, result.duplicateKeys().get(0).get(1).token().range().beginning().line());
    assertEquals(31, result.duplicateKeys().get(0).get(1).token().range().beginning().column());
    assertEquals(1, result.duplicateKeys().get(0).get(1).token().range().end().line());
    assertEquals(36, result.duplicateKeys().get(0).get(1).token().range().end().column());
    assertEquals("key1", result.duplicateKeys().get(0).get(2).value());
    assertEquals(1, result.duplicateKeys().get(0).get(2).token().range().beginning().line());
    assertEquals(45, result.duplicateKeys().get(0).get(2).token().range().beginning().column());
    assertEquals(1, result.duplicateKeys().get(0).get(2).token().range().end().line());
    assertEquals(50, result.duplicateKeys().get(0).get(2).token().range().end().column());

    assertEquals(2, result.duplicateKeys().get(1).size());
    assertEquals("key2", result.duplicateKeys().get(1).get(0).value());
    assertEquals(1, result.duplicateKeys().get(1).get(0).token().range().beginning().line());
    assertEquals(16, result.duplicateKeys().get(1).get(0).token().range().beginning().column());
    assertEquals(1, result.duplicateKeys().get(1).get(0).token().range().end().line());
    assertEquals(21, result.duplicateKeys().get(1).get(0).token().range().end().column());
    assertEquals("key2", result.duplicateKeys().get(1).get(1).value());
    assertEquals(1, result.duplicateKeys().get(1).get(1).token().range().beginning().line());
    assertEquals(60, result.duplicateKeys().get(1).get(1).token().range().beginning().column());
    assertEquals(1, result.duplicateKeys().get(1).get(1).token().range().end().line());
    assertEquals(65, result.duplicateKeys().get(1).get(1).token().range().end().column());

    JSONValueObject root = (JSONValueObject) result.root();
    assertNotNull(root);
    assertEquals(JSONValueBoolean.FALSE, root.get("key1"));
    assertEquals(JSONValueNull.INSTANCE, root.get("key2"));
  }

  @Test
  public void testTooBigNumberForDouble() {
    JSONParserErrorHandlingOptions opt =
        JSONParserErrorHandlingOptions.builder()
            .showErrorLine(true)
            .failOnTooBigNumberForDouble(true)
            .build();

    JSONText jsText = JSONText.fromString("{\"key1\": 1.52, \"key2\": 1e309, \"key1\": null}");

    JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(opt));
    log.info(ex::getMessage);
  }

  // TODO: test too big number for double
}
