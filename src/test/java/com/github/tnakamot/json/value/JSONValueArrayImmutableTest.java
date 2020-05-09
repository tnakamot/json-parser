package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JSONValueArrayImmutableTest {
  @Test
  public void testVariousMethod() {
    JSONValueArrayMutable arrayM = new JSONValueArrayMutable();
    assertTrue(arrayM.add(3.15));
    assertTrue(arrayM.add(123));
    assertTrue(arrayM.add("value"));
    assertTrue(arrayM.add(JSONValueNull.INSTANCE));
    assertTrue(arrayM.add(true));
    assertTrue(arrayM.add(false));

    JSONValueArrayImmutable array = arrayM.toImmutable();

    assertTrue(array.contains(new JSONValueNumber(3.15)));
    assertTrue(array.contains(new JSONValueNumber(123)));
    assertTrue(array.contains(new JSONValueString("value")));
    assertTrue(array.contains(JSONValueNull.INSTANCE));
    assertTrue(array.contains(JSONValueBoolean.TRUE));
    assertTrue(array.contains(JSONValueBoolean.FALSE));

    assertFalse(array.contains(new JSONValueNumber(1.23)));
    assertFalse(array.contains(new JSONValueNumber(999)));
    assertFalse(array.contains(new JSONValueString("VALUE")));

    assertEquals(0, array.indexOf(new JSONValueNumber(3.15)));
    assertEquals(1, array.indexOf(new JSONValueNumber(123)));
    assertEquals(2, array.indexOf(new JSONValueString("value")));
    assertEquals(3, array.indexOf(JSONValueNull.INSTANCE));
    assertEquals(4, array.indexOf(JSONValueBoolean.TRUE));
    assertEquals(5, array.indexOf(JSONValueBoolean.FALSE));
    assertEquals(-1, array.indexOf(new JSONValueNumber(3.151)));

    assertEquals(0, array.lastIndexOf(new JSONValueNumber(3.15)));
    assertEquals(1, array.lastIndexOf(new JSONValueNumber(123)));
    assertEquals(2, array.lastIndexOf(new JSONValueString("value")));
    assertEquals(3, array.lastIndexOf(JSONValueNull.INSTANCE));
    assertEquals(4, array.lastIndexOf(JSONValueBoolean.TRUE));
    assertEquals(5, array.lastIndexOf(JSONValueBoolean.FALSE));
    assertEquals(-1, array.lastIndexOf(new JSONValueNumber(3.151)));

    JSONValue[] values = array.toArray(new JSONValue[0]);
    assertEquals(new JSONValueNumber(3.15), values[0]);
    assertEquals(new JSONValueNumber(123), values[1]);
    assertEquals(new JSONValueString("value"), values[2]);
    assertEquals(JSONValueNull.INSTANCE, values[3]);
    assertEquals(JSONValueBoolean.TRUE, values[4]);
    assertEquals(JSONValueBoolean.FALSE, values[5]);

    Object[] objects = array.toArray();
    assertEquals(new JSONValueNumber(3.15), objects[0]);
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
  }

  @Test
  public void testWithDuplicate() {
    JSONValueArrayMutable arrayM = new JSONValueArrayMutable();
    assertTrue(arrayM.add(3.15));
    assertTrue(arrayM.add(123));
    assertTrue(arrayM.add(3.15));
    assertTrue(arrayM.add(123));
    assertTrue(arrayM.add(3.15));
    assertTrue(arrayM.add(999));
    assertTrue(arrayM.add(999));

    JSONValueArrayImmutable array = arrayM.toImmutable();

    assertTrue(array.contains(new JSONValueNumber(3.15)));
    assertTrue(array.contains(new JSONValueNumber(123)));
    assertTrue(array.contains(new JSONValueNumber(999)));
    assertFalse(array.contains(new JSONValueNumber(1234)));

    JSONValueArrayMutable arrayContained = new JSONValueArrayMutable();
    arrayContained.add(123);
    assertTrue(array.containsAll(arrayContained));
    arrayContained.add(3.15);
    assertTrue(array.containsAll(arrayContained));
    arrayContained.add(1.23);
    assertFalse(array.containsAll(arrayContained));

    assertEquals(0, array.indexOf(new JSONValueNumber(3.15)));
    assertEquals(1, array.indexOf(new JSONValueNumber(123)));
    assertEquals(5, array.indexOf(new JSONValueNumber(999)));
    assertEquals(4, array.lastIndexOf(new JSONValueNumber(3.15)));
    assertEquals(3, array.lastIndexOf(new JSONValueNumber(123)));
    assertEquals(6, array.lastIndexOf(new JSONValueNumber(999)));
  }

  @SuppressWarnings("deprecation")
  @Test
  public void testUnsupportedMethod() {
    JSONValueArrayMutable arrayM = new JSONValueArrayMutable();
    assertTrue(arrayM.add(3.15));
    assertTrue(arrayM.add(123));
    assertTrue(arrayM.add("value"));
    assertTrue(arrayM.add(JSONValueNull.INSTANCE));
    assertTrue(arrayM.add(true));
    assertTrue(arrayM.add(false));

    JSONValueArrayImmutable array = arrayM.toImmutable();

    assertThrows(UnsupportedOperationException.class, array::clear);
    assertThrows(UnsupportedOperationException.class, () -> array.add(123));
    assertThrows(UnsupportedOperationException.class, () -> array.add(3.15));
    assertThrows(UnsupportedOperationException.class, () -> array.add(true));
    assertThrows(UnsupportedOperationException.class, () -> array.add("value"));
    assertThrows(UnsupportedOperationException.class, () -> array.add(JSONValueNull.INSTANCE));
    assertThrows(UnsupportedOperationException.class, () -> array.add(1, new JSONValueNumber(123)));
    assertThrows(
        UnsupportedOperationException.class, () -> array.set(0, new JSONValueNumber(3.15)));
    assertThrows(UnsupportedOperationException.class, () -> array.remove(2));
    assertThrows(
        UnsupportedOperationException.class, () -> array.remove(new JSONValueString("value")));
    assertThrows(UnsupportedOperationException.class, () -> array.remove(1));

    JSONValueArray values = new JSONValueArrayMutable();
    values.add(1000);
    values.add(3.15);
    assertThrows(UnsupportedOperationException.class, () -> array.removeAll(values));
    assertThrows(UnsupportedOperationException.class, () -> array.retainAll(values));
    assertThrows(UnsupportedOperationException.class, () -> array.addAll(values));
    assertThrows(UnsupportedOperationException.class, () -> array.addAll(1, values));
  }
}
