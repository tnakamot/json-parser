package com.github.tnakamot.json.value;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueTest {
    @Test
    public void testJSONValueArray() {
        JSONValue value = new JSONValueArray(new ArrayList<>());
        assertEquals(JSONValueType.ARRAY, value.type());
    }

    @Test
    public void testJSONValueBoolean() {
        JSONValue value1 = JSONValueBoolean.valueOf(true);
        assertEquals(JSONValueType.BOOLEAN, value1.type());

        JSONValue value2 = JSONValueBoolean.valueOf(false);
        assertEquals(JSONValueType.BOOLEAN, value2.type());

        JSONValue value3 = JSONValueBoolean.TRUE;
        assertEquals(JSONValueType.BOOLEAN, value3.type());

        JSONValue value4 = JSONValueBoolean.FALSE;
        assertEquals(JSONValueType.BOOLEAN, value4.type());
    }

    @Test
    public void testJSONValueNull() {
        JSONValue value = JSONValueNull.INSTANCE;
        assertEquals(JSONValueType.NULL, value.type());
    }

    @Test
    public void testJSONValueNumber() {
        JSONValue value = new JSONValueNumber("1");
        assertEquals(JSONValueType.NUMBER, value.type());
    }

    @Test
    public void testJSONValueObject() {
        JSONValue value = new JSONValueObject(new HashMap<>());
        assertEquals(JSONValueType.OBJECT, value.type());
    }

    @Test
    public void testJSONValueString() {
        JSONValue value = new JSONValueString("abc");
        assertEquals(JSONValueType.STRING, value.type());
    }
}
