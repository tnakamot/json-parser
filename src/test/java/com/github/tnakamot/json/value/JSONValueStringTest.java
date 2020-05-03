package com.github.tnakamot.json.value;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueStringTest {
    @Test
    public void testType() throws IOException, JSONParserException {
        JSONValueString val1 = new JSONValueString("abc");
        JSONValueString val2 = (JSONValueString) JSONText.fromString("\"def\"").parse();

        assertEquals(JSONValueType.STRING, val1.type());
        assertEquals(JSONValueType.STRING, val2.type());
    }

    @Test
    public void testEquality() throws IOException, JSONParserException {
        JSONValueString val1 = new JSONValueString("abc");
        JSONValueString val2 = new JSONValueString("abc");
        JSONValueString val3 = (JSONValueString) JSONText.fromString("\"abc\"").parse();

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
        JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse();

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
        JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse();

        assertEquals("abc", val1.value());
        assertEquals("xyz", val2.value());
        assertEquals("hello", val3.value());
    }

    @Test
    public void testToString() throws IOException, JSONParserException {
        JSONValueString val1 = new JSONValueString("abc");
        JSONValueString val2 = new JSONValueString("xyz");
        JSONValueString val3 = (JSONValueString) JSONText.fromString("\"hello\"").parse();

        assertEquals("abc", val1.toString());
        assertEquals("xyz", val2.toString());
        assertEquals("hello", val3.toString());
    }
}
