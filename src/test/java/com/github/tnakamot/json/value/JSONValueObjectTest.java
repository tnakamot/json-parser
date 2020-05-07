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
}
