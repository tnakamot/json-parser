package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONValueObjectTest {
  private static final Logger log = LoggerFactory.getLogger(JSONValueObjectTest.class);

  @Test
  public void testPut1() {
    JSONValueObject obj = new JSONValueObjectMutable();

    JSONValue toAdd1 = new JSONValueNumber("123");
    JSONValue toAdd2 = new JSONValueNumber("456");
    JSONValue toAdd3 = new JSONValueNumber("789");

    assertNull(obj.put("key1", toAdd1));
    assertNull(obj.put("key2", toAdd2));
    assertEquals(toAdd2, obj.put("key2", toAdd3));

    assertEquals(2, obj.size());
    assertTrue(obj.containsKey("key1"));
    assertTrue(obj.containsKey("key2"));
    assertFalse(obj.containsKey("key3"));

    assertTrue(obj.containsKey(new JSONValueString("key1")));
    assertTrue(obj.containsKey(new JSONValueString("key2")));
    assertFalse(obj.containsKey(new JSONValueString("key3")));

    assertEquals(123, ((JSONValueNumber) obj.get("key1")).toLong());
    assertEquals(789, ((JSONValueNumber) obj.get("key2")).toLong());
    assertNull(obj.get("key3"));

    assertEquals(123, ((JSONValueNumber) obj.get(new JSONValueString("key1"))).toLong());
    assertEquals(789, ((JSONValueNumber) obj.get(new JSONValueString("key2"))).toLong());
    assertNull(obj.get(new JSONValueString("key3")));

    assertEquals("{\"key1\":123,\"key2\":789}", obj.toTokenString());
    log.info(() -> obj.toTokenString("\n", "  "));
  }

  @Test
  public void testPut2() {
    JSONValueObject obj = new JSONValueObjectMutable();

    assertNull(obj.put("key1", true));
    assertNull(obj.put("key2", false));
    assertNull(obj.put("key3", 9.99));
    assertNull(obj.put("key4", 512));
    assertNull(obj.put("key5", "hello"));
    assertNull(obj.put("key6", (String) null));

    assertEquals(6, obj.size());
    assertTrue(obj.containsKey("key1"));
    assertTrue(obj.containsKey("key2"));
    assertTrue(obj.containsKey("key3"));
    assertTrue(obj.containsKey("key4"));
    assertTrue(obj.containsKey("key5"));
    assertTrue(obj.containsKey("key6"));

    assertTrue(((JSONValueBoolean) obj.get("key1")).value());
    assertFalse(((JSONValueBoolean) obj.get("key2")).value());
    assertEquals(9.99, ((JSONValueNumber) obj.get("key3")).toDouble());
    assertEquals(512, ((JSONValueNumber) obj.get("key4")).toLong());
    assertEquals("hello", ((JSONValueString) obj.get("key5")).value());
    assertEquals("", ((JSONValueString) obj.get("key6")).value());

    assertEquals(
        "{\"key1\":true,\"key2\":false,\"key3\":9.99,\"key4\":512,\"key5\":\"hello\",\"key6\":\"\"}",
        obj.toTokenString());
  }

  @Test
  public void testGet1() throws IOException, JSONParserException {
    JSONValueObject root =
        (JSONValueObject)
            JSONText.fromString(
                    "{"
                        + "\"key1\": true,"
                        + "\"key2\": 1.53,"
                        + "\"key3\": 512,"
                        + "\"key4\": \"hello\","
                        + "\"key5\": [],"
                        + "\"key6\": {}"
                        + "}")
                .parse(true);

    assertTrue(root.getBoolean("key1"));
    assertEquals(1.53, root.getDouble("key2"));
    assertEquals(512, root.getLong("key3"));
    assertEquals("hello", root.getString("key4"));
    assertEquals(0, root.getArray("key5").size());
    assertEquals(0, root.getObject("key6").size());

    WrongValueTypeException ex;
    ex = assertThrows(WrongValueTypeException.class, () -> root.getBoolean("key2"));
    assertEquals(JSONValueType.BOOLEAN, ex.expected());
    assertEquals(JSONValueType.NUMBER, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getLong("key1"));
    assertEquals(JSONValueType.NUMBER, ex.expected());
    assertEquals(JSONValueType.BOOLEAN, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getDouble("key4"));
    assertEquals(JSONValueType.NUMBER, ex.expected());
    assertEquals(JSONValueType.STRING, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getString("key3"));
    assertEquals(JSONValueType.STRING, ex.expected());
    assertEquals(JSONValueType.NUMBER, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getArray("key6"));
    assertEquals(JSONValueType.ARRAY, ex.expected());
    assertEquals(JSONValueType.OBJECT, ex.actual());
    log.info(ex, ex::getMessage);

    ex = assertThrows(WrongValueTypeException.class, () -> root.getObject("key5"));
    assertEquals(JSONValueType.OBJECT, ex.expected());
    assertEquals(JSONValueType.ARRAY, ex.actual());
    log.info(ex, ex::getMessage);

    NumberFormatException ex2 =
        assertThrows(NumberFormatException.class, () -> root.getLong("key2"));
    log.info(ex2, ex2::getMessage);

    assertThrows(IllegalArgumentException.class, () -> root.getBoolean("key7"));
    assertThrows(IllegalArgumentException.class, () -> root.getLong("key7"));
    assertThrows(IllegalArgumentException.class, () -> root.getDouble("key7"));
    assertThrows(IllegalArgumentException.class, () -> root.getString("key7"));
    assertThrows(IllegalArgumentException.class, () -> root.getArray("key7"));
    assertThrows(IllegalArgumentException.class, () -> root.getObject("key7"));
  }

  @Test
  public void testGe2() throws IOException, JSONParserException {
    JSONText jsText =
        JSONText.fromString(
            " { \"key1\": \"value\", "
                + "   \"key2\": 3.14, "
                + "   \"key3\": false,"
                + "   \"key4\": 1024,"
                + "   \"key5\": [5, 1, 2], "
                + "   \"key6\": {\"key6-1\": 0} }");
    JSONValueObject root = (JSONValueObject) jsText.parse();

    String value1Str = root.getString("key1");
    System.out.println(value1Str);

    double value2Dbl = root.getDouble("key2");
    System.out.println(value2Dbl);

    boolean value3Bool = root.getBoolean("key3");
    System.out.println(value3Bool);

    long value4Lng = root.getLong("key4");
    System.out.println(value4Lng);

    JSONValueArray value5Arr = root.getArray("key5");
    System.out.println(value5Arr.getLong(0));
    System.out.println(value5Arr.getLong(1));
    System.out.println(value5Arr.getLong(2));

    JSONValueObject value6Obj = root.getObject("key6");
    System.out.println(value6Obj.getLong("key6-1"));
  }

  @Test
  public void testRemove() {
    JSONValueObjectMutable obj = new JSONValueObjectMutable();

    JSONValue toAdd1 = new JSONValueNumber("123");
    JSONValue toAdd2 = new JSONValueNumber("456");

    assertNull(obj.put("key1", toAdd1));
    assertNull(obj.put("key2", toAdd2));

    assertEquals(2, obj.size());

    assertNull(obj.remove("key3"));
    assertEquals(2, obj.size());

    assertEquals(toAdd1, obj.remove("key1"));
    assertEquals(1, obj.size());

    assertEquals(toAdd2, obj.remove(new JSONValueString("key2")));
    assertEquals(0, obj.size());
  }

  @Test
  public void testToImmutable() {
    JSONValueObjectMutable rootObj = new JSONValueObjectMutable();
    JSONValueObjectMutable childObj = new JSONValueObjectMutable();
    JSONValueArrayMutable childArray = new JSONValueArrayMutable();

    rootObj.put("childObj", childObj);
    rootObj.put("childArray", childArray);

    JSONValueObject rootObjImmutable = rootObj.toImmutable();
    assertTrue(rootObjImmutable.get("childObj") instanceof JSONValueObjectImmutable);
    assertTrue(rootObjImmutable.get("childArray") instanceof JSONValueArrayImmutable);

    UnsupportedOperationException ex1 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> rootObjImmutable.put("test", new JSONValueString("hey")));
    log.info(ex1, ex1::getMessage);

    UnsupportedOperationException ex2 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
              JSONValueObject childObjImmutable =
                  (JSONValueObject) rootObjImmutable.get("childObj");
              childObjImmutable.clear();
            });
    log.info(ex2, ex2::getMessage);

    UnsupportedOperationException ex3 =
        assertThrows(
            UnsupportedOperationException.class,
            () -> {
              JSONValueArray childArrayImmutable =
                  (JSONValueArray) rootObjImmutable.get("childArray");
              childArrayImmutable.add(new JSONValueNumber("1e5"));
            });
    log.info(ex3, ex3::getMessage);
  }

  @Test
  public void testToMutable() throws IOException, JSONParserException {
    JSONValue root =
        JSONText.fromString("{\"key1\": [true, 123], \"key2\": {\"key3\": 5.2}}").parse(true);
    assertTrue(root instanceof JSONValueObjectImmutable);

    JSONValueObjectImmutable rootObj = (JSONValueObjectImmutable) root;
    JSONValueObject rootObjMutable = rootObj.toMutable();

    ((JSONValueArray) rootObjMutable.get("key1")).add(JSONValueNull.INSTANCE);
    ((JSONValueObject) rootObjMutable.get("key2")).put("key4", new JSONValueNumber("3.14"));
    rootObjMutable.put("key5", new JSONValueNumber("512"));

    assertEquals(
        "{\"key1\":[true,123,null],\"key2\":{\"key3\":5.2,\"key4\":3.14},\"key5\":512}",
        rootObjMutable.toTokenString());
  }

  @Test
  public void testEqualityMutable() throws IOException, JSONParserException {
    JSONValue obj1 =
        JSONText.fromString("{\"key1\": [true, 123], \"key2\": {\"key3\": 5.2}}").parse(false);
    JSONValueObject obj2 = new JSONValueObjectMutable();
    obj2.put("key1", new JSONValueArrayMutable());
    obj2.getArray("key1").add(true);
    obj2.getArray("key1").add(123);
    obj2.put("key2", new JSONValueObjectMutable());
    obj2.getObject("key2").put("key3", 5.2);

    assertEquals(obj1, obj2);
  }

  @Test
  public void testEqualityImmutable() throws IOException, JSONParserException {
    JSONValue obj1 =
        JSONText.fromString("{\"key1\": [true, 123], \"key2\": {\"key3\": 5.2}}").parse(true);
    JSONValueObjectMutable obj2 = new JSONValueObjectMutable();
    obj2.put("key1", new JSONValueArrayMutable());
    obj2.getArray("key1").add(true);
    obj2.getArray("key1").add(123);
    obj2.put("key2", new JSONValueObjectMutable());
    obj2.getObject("key2").put("key3", 5.2);

    assertEquals(obj1, obj2.toImmutable());
  }
}
