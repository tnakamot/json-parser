package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JSONValueArrayMutableTest {

  @Test
  public void testVariousMethods() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();
    assertTrue(array.add(3.14));
    assertTrue(array.add(123));
    assertTrue(array.add("value"));
    assertTrue(array.add(JSONValueNull.INSTANCE));
    assertTrue(array.add(true));
    assertTrue(array.add(false));

    assertTrue(array.contains(new JSONValueNumber(3.14)));
    assertTrue(array.contains(new JSONValueNumber(123)));
    assertTrue(array.contains(new JSONValueString("value")));
    assertTrue(array.contains(JSONValueNull.INSTANCE));
    assertTrue(array.contains(JSONValueBoolean.TRUE));
    assertTrue(array.contains(JSONValueBoolean.FALSE));

    assertFalse(array.contains(new JSONValueNumber(1.23)));
    assertFalse(array.contains(new JSONValueNumber(999)));
    assertFalse(array.contains(new JSONValueString("VALUE")));

    assertEquals(0, array.indexOf(new JSONValueNumber(3.14)));
    assertEquals(1, array.indexOf(new JSONValueNumber(123)));
    assertEquals(2, array.indexOf(new JSONValueString("value")));
    assertEquals(3, array.indexOf(JSONValueNull.INSTANCE));
    assertEquals(4, array.indexOf(JSONValueBoolean.TRUE));
    assertEquals(5, array.indexOf(JSONValueBoolean.FALSE));
    assertEquals(-1, array.indexOf(new JSONValueNumber(3.141)));

    assertEquals(0, array.lastIndexOf(new JSONValueNumber(3.14)));
    assertEquals(1, array.lastIndexOf(new JSONValueNumber(123)));
    assertEquals(2, array.lastIndexOf(new JSONValueString("value")));
    assertEquals(3, array.lastIndexOf(JSONValueNull.INSTANCE));
    assertEquals(4, array.lastIndexOf(JSONValueBoolean.TRUE));
    assertEquals(5, array.lastIndexOf(JSONValueBoolean.FALSE));
    assertEquals(-1, array.lastIndexOf(new JSONValueNumber(3.141)));

    JSONValue[] values = array.toArray(new JSONValue[0]);
    assertEquals(new JSONValueNumber(3.14), values[0]);
    assertEquals(new JSONValueNumber(123), values[1]);
    assertEquals(new JSONValueString("value"), values[2]);
    assertEquals(JSONValueNull.INSTANCE, values[3]);
    assertEquals(JSONValueBoolean.TRUE, values[4]);
    assertEquals(JSONValueBoolean.FALSE, values[5]);

    Object[] objects = array.toArray();
    assertEquals(new JSONValueNumber(3.14), objects[0]);
    assertEquals(new JSONValueNumber(123), objects[1]);
    assertEquals(new JSONValueString("value"), objects[2]);
    assertEquals(JSONValueNull.INSTANCE, objects[3]);
    assertEquals(JSONValueBoolean.TRUE, objects[4]);
    assertEquals(JSONValueBoolean.FALSE, objects[5]);

    JSONValueArray subArray = new JSONValueArrayMutable(array.subList(1, 4));
    assertEquals(3, subArray.size());
    assertEquals(123, subArray.getLong(0));
    assertEquals("value", subArray.getString(1));
    assertEquals(JSONValueNull.INSTANCE, subArray.get(2));

    assertFalse(array.remove(new JSONValueNumber(1.23)));
    assertEquals(6, array.size());

    assertTrue(array.remove(new JSONValueNumber(3.14)));
    assertEquals(5, array.size());
    assertEquals(123, array.getLong(0));
    assertFalse(array.getBoolean(4));

    assertEquals(new JSONValueNumber(123), array.set(0, new JSONValueNumber(999)));
    assertEquals(new JSONValueNumber(999), array.get(0));
    assertEquals(5, array.size());

    array.add(0, new JSONValueNumber(3.14));
    assertEquals(new JSONValueNumber(3.14), array.get(0));
    assertEquals(new JSONValueNumber(999), array.get(1));
    assertEquals(6, array.size());

    assertFalse(array.isEmpty());

    array.clear();
    assertEquals(0, array.size());
    assertTrue(array.isEmpty());
  }

  @Test
  public void testWithDuplicate() {
    JSONValueArrayMutable array = new JSONValueArrayMutable();
    assertTrue(array.add(3.14));
    assertTrue(array.add(123));
    assertTrue(array.add(3.14));
    assertTrue(array.add(123));
    assertTrue(array.add(3.14));
    assertTrue(array.add(999));
    assertTrue(array.add(999));

    assertTrue(array.contains(new JSONValueNumber(3.14)));
    assertTrue(array.contains(new JSONValueNumber(123)));
    assertTrue(array.contains(new JSONValueNumber(999)));
    assertFalse(array.contains(new JSONValueNumber(1234)));

    JSONValueArrayMutable arrayContained = new JSONValueArrayMutable();
    arrayContained.add(123);
    assertTrue(array.containsAll(arrayContained));
    arrayContained.add(3.14);
    assertTrue(array.containsAll(arrayContained));
    arrayContained.add(1.23);
    assertFalse(array.containsAll(arrayContained));

    assertEquals(0, array.indexOf(new JSONValueNumber(3.14)));
    assertEquals(1, array.indexOf(new JSONValueNumber(123)));
    assertEquals(5, array.indexOf(new JSONValueNumber(999)));
    assertEquals(4, array.lastIndexOf(new JSONValueNumber(3.14)));
    assertEquals(3, array.lastIndexOf(new JSONValueNumber(123)));
    assertEquals(6, array.lastIndexOf(new JSONValueNumber(999)));

    assertTrue(array.remove(new JSONValueNumber(3.14)));
    assertEquals(6, array.size());
    assertEquals(new JSONValueNumber(123), array.get(0));
    assertEquals(new JSONValueNumber(3.14), array.get(1));
    assertEquals(new JSONValueNumber(123), array.get(2));
    assertEquals(new JSONValueNumber(3.14), array.get(3));
    assertEquals(new JSONValueNumber(999), array.get(4));
    assertEquals(new JSONValueNumber(999), array.get(5));

    JSONValueArray valuesToRemove = new JSONValueArrayMutable();
    valuesToRemove.add(1000);
    valuesToRemove.add(1.23);
    assertFalse(array.removeAll(valuesToRemove));
    assertEquals(6, array.size());

    valuesToRemove.add(3.14);
    valuesToRemove.add(123);
    assertTrue(array.removeAll(valuesToRemove));
    assertEquals(2, array.size());
    assertEquals(new JSONValueNumber(999), array.get(0));
    assertEquals(new JSONValueNumber(999), array.get(1));
  }
}
