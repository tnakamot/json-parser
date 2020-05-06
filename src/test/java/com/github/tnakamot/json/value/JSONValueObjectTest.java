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
  public void testPut() {
    JSONValueObjectMutable obj = new JSONValueObjectMutable();
    obj.put("key1", new JSONValueNumber("123"));
    obj.put(new JSONValueString("key2"), new JSONValueNumber("789"));

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
  public void testToImmutable() {
    JSONValueObjectMutable rootObj = new JSONValueObjectMutable();
    JSONValueObjectMutable childObj = new JSONValueObjectMutable();
    JSONValueArrayMutable childArray = new JSONValueArrayMutable();

    rootObj.put("childObj", childObj);
    rootObj.put("childArray", childArray);

    JSONValueObject rootObjImmutable = rootObj.toImmutable();
    assertTrue(rootObjImmutable instanceof JSONValueObjectImmutable);
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
