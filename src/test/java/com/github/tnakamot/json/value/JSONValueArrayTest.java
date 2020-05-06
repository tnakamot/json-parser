package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
  public void testAdd() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();
    array.add(new JSONValueString("test"));
    array.add(JSONValueBoolean.TRUE);
    array.add(new JSONValueNumber("3.14"));
    array.add(JSONValueNull.INSTANCE);

    assertEquals(4, array.size());
    assertEquals(new JSONValueString("test"), array.get(0));
    assertEquals(JSONValueBoolean.TRUE, array.get(1));
    assertEquals(new JSONValueNumber("3.14"), array.get(2));
    assertEquals(JSONValueNull.INSTANCE, array.get(3));

    assertEquals("[\"test\",true,3.14,null]", array.toTokenString());
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
}
