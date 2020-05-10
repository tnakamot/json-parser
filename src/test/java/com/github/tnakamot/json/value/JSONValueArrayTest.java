package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONValueArrayTest {
  private static final Logger log = LoggerFactory.getLogger(JSONValueObjectTest.class);

  @Test
  public void testAdd1() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();
    assertTrue(array.add(new JSONValueString("test")));
    assertTrue(array.add(JSONValueBoolean.TRUE));
    assertTrue(array.add(new JSONValueNumber("3.15")));
    assertTrue(array.add(JSONValueNull.INSTANCE));

    assertEquals(4, array.size());
    assertFalse(array.isEmpty());
    assertEquals(new JSONValueString("test"), array.get(0));
    assertEquals(JSONValueBoolean.TRUE, array.get(1));
    assertEquals(new JSONValueNumber("3.15"), array.get(2));
    assertEquals(JSONValueNull.INSTANCE, array.get(3));

    assertEquals("[\"test\",true,3.15,null]", array.toTokenString());
  }

  @Test
  public void testAdd2() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();

    assertTrue(array.add(true));
    assertTrue(array.add(false));
    assertTrue(array.add("hello"));
    assertTrue(array.add(1024));
    assertTrue(array.add(3.15));
    assertTrue(array.add((String) null));

    assertEquals(6, array.size());
    assertFalse(array.isEmpty());
    assertEquals(JSONValueBoolean.TRUE, array.get(0));
    assertEquals(JSONValueBoolean.FALSE, array.get(1));
    assertEquals(new JSONValueString("hello"), array.get(2));
    assertEquals(new JSONValueNumber(1024), array.get(3));
    assertEquals(new JSONValueNumber(3.15), array.get(4));
    assertEquals(new JSONValueString(""), array.get(5));

    assertEquals("[true,false,\"hello\",1024,3.15,\"\"]", array.toTokenString());
  }

  @Test
  public void testGet1() throws IOException, JSONParserException {
    JSONValueArray root =
        (JSONValueArray) JSONText.fromString("[true,1.53,512,\"hello\",[],{}]").parse(true);

    assertNotNull(root);
    assertTrue(root.getBoolean(0));
    assertEquals(1.53, root.getDouble(1));
    assertEquals(512, root.getLong(2));
    assertEquals("hello", root.getString(3));
    assertEquals(0, root.getArray(4).size());
    assertTrue(root.getArray(4).isEmpty());
    assertEquals(0, root.getObject(5).size());
    assertTrue(root.getObject(5).isEmpty());

    WrongValueTypeException ex;
    ex = assertThrows(WrongValueTypeException.class, () -> root.getBoolean(1));
    assertEquals(JSONValueType.BOOLEAN, ex.expected());
    assertEquals(JSONValueType.NUMBER, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getLong(0));
    assertEquals(JSONValueType.NUMBER, ex.expected());
    assertEquals(JSONValueType.BOOLEAN, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getDouble(3));
    assertEquals(JSONValueType.NUMBER, ex.expected());
    assertEquals(JSONValueType.STRING, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getString(2));
    assertEquals(JSONValueType.STRING, ex.expected());
    assertEquals(JSONValueType.NUMBER, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getArray(5));
    assertEquals(JSONValueType.ARRAY, ex.expected());
    assertEquals(JSONValueType.OBJECT, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getObject(4));
    assertEquals(JSONValueType.OBJECT, ex.expected());
    assertEquals(JSONValueType.ARRAY, ex.actual());
    log.info(ex, ex::getMessage);

    NumberFormatException ex2 = assertThrows(NumberFormatException.class, () -> root.getLong(1));
    log.info(ex2, ex2::getMessage);

    assertThrows(IndexOutOfBoundsException.class, () -> root.getBoolean(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getLong(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getDouble(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getString(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getArray(-1));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getObject(-1));

    assertThrows(IndexOutOfBoundsException.class, () -> root.getBoolean(6));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getLong(6));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getDouble(6));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getString(6));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getArray(6));
    assertThrows(IndexOutOfBoundsException.class, () -> root.getObject(6));
  }

  @Test
  public void testToImmutable() {
    JSONValueArrayMutable rootArray = new JSONValueArrayMutable();
    JSONValueObjectMutable childObj = new JSONValueObjectMutable();
    JSONValueArrayMutable childArray = new JSONValueArrayMutable();

    rootArray.add(childObj);
    rootArray.add(childArray);

    JSONValueArray rootArrayImmutable = rootArray.toImmutable();
    assertTrue(rootArrayImmutable.get(0) instanceof JSONValueObjectImmutable);
    assertTrue(rootArrayImmutable.get(1) instanceof JSONValueArrayImmutable);

    UnsupportedOperationException ex1 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> rootArrayImmutable.add(new JSONValueString("test")));
    log.info(ex1, ex1::getMessage);

    UnsupportedOperationException ex2 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
              JSONValueObject childObjImmutable = (JSONValueObject) rootArrayImmutable.get(0);
              childObjImmutable.clear();
            });
    log.info(ex2, ex2::getMessage);

    UnsupportedOperationException ex3 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
              JSONValueArray childArrayImmutable = (JSONValueArray) rootArrayImmutable.get(1);
              childArrayImmutable.add(new JSONValueNumber("1e5"));
            });
    log.info(ex3, ex3::getMessage);
  }

  @Test
  public void testToMutable() throws IOException, JSONParserException {
    JSONValue root = JSONText.fromString("[[true, 123], {\"key1\": 5.2}]").parse(true);
    assertTrue(root instanceof JSONValueArrayImmutable);

    JSONValueArrayImmutable rootArray = (JSONValueArrayImmutable) root;
    JSONValueArray rootArrayMutable = rootArray.toMutable();

    ((JSONValueArray) rootArrayMutable.get(0)).add(JSONValueNull.INSTANCE);
    ((JSONValueObject) rootArrayMutable.get(1)).put("key2", JSONValueBoolean.FALSE);
    rootArrayMutable.add(new JSONValueString("test"));

    assertEquals(
        "[[true,123,null],{\"key1\":5.2,\"key2\":false},\"test\"]",
        rootArrayMutable.toTokenString());
  }

  @Test
  public void testEquality() throws IOException, JSONParserException {
    JSONValue array1 = JSONText.fromString("[[true, 123], {\"key1\": 5.2}]").parse();
    assertNotNull(array1);

    JSONValueArray array2 = new JSONValueArrayMutable();
    array2.add(new JSONValueArrayMutable());
    array2.getArray(0).add(true);
    array2.getArray(0).add(123);
    array2.add(new JSONValueObjectMutable());
    array2.getObject(1).put("key1", 5.2);

    assertEquals(array1.hashCode(), array2.hashCode());
    assertEquals(array2, array1);
  }

  @Test
  public void testInequality() throws IOException, JSONParserException {
    JSONValue array1 = JSONText.fromString("[[true, 123], {\"key1\": 5.2}]").parse();

    assertNotEquals(array1, JSONValueNull.INSTANCE);
    assertNotEquals(array1, JSONValueBoolean.TRUE);
    assertNotEquals(array1, JSONValueBoolean.FALSE);
    assertNotEquals(array1, new JSONValueNumber(5.2));
    assertNotEquals(array1, new JSONValueNumber(134));
    assertNotEquals(array1, new JSONValueString("key1"));
    assertNotEquals(array1, new JSONValueObjectMutable());

    JSONValueArray array2 = new JSONValueArrayMutable();
    array2.add(new JSONValueArrayMutable());
    array2.getArray(0).add(true);
    array2.getArray(0).add(123);
    assertNotEquals(array1, array2);

    array2.add(new JSONValueNumber(5.2));
    assertNotEquals(array1, array2);

    array2.remove(1);
    array2.add(new JSONValueObjectMutable());
    array2.getObject(1).put("key1", 5.2);
    assertEquals(array1, array2);

    array2.add(5.2);
    assertNotEquals(array1, array2);
  }

  @Test
  public void testEmpty() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();
    assertEquals(0, array.size());
    assertTrue(array.isEmpty());
    assertEquals("[]", array.toTokenString());
    assertEquals("[ ]", array.toTokenString("\n", "  "));
  }
}
