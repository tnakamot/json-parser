package com.github.tnakamot.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.value.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

public class JSONParserTest {
    // TODO: more test cases especially for error cases.

    @Test
    public void testEmpty() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString("").parse();
        assertNull(root);
    }

    @Test
    public void testWSOnly() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" \r\n\t").parse();
        assertNull(root);
    }

    @Test
    public void testEmptyObject() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" { } ").parse();

        assertEquals(JSONValueType.OBJECT, root.type());
        assertTrue(root instanceof JSONValueObject);

        JSONValueObject rootObj = (JSONValueObject) root;

        assertEquals(0, rootObj.size());
        assertFalse(rootObj.containsKey("key"));
        assertTrue(rootObj.isEmpty());
    }

    @Test
    public void testEmptyArray() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString("[]").parse();

        assertEquals(JSONValueType.ARRAY, root.type());
        assertTrue(root instanceof JSONValueArray);

        JSONValueArray rootArray = (JSONValueArray) root;

        assertEquals(0, rootArray.size());
        assertFalse(rootArray.contains(JSONValueNull.INSTANCE));
        assertTrue(rootArray.isEmpty());
    }

    @Test
    public void testNullOnly() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString("null").parse();

        assertEquals(JSONValueType.NULL, root.type());
        assertTrue(root instanceof JSONValueNull);
        assertEquals(JSONValueNull.INSTANCE, root);
        assertEquals("null", root.toString());
    }

    @Test
    public void testTrueOnly() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" true ").parse();

        assertEquals(JSONValueType.BOOLEAN, root.type());
        assertTrue(root instanceof JSONValueBoolean);
        assertEquals(JSONValueBoolean.TRUE, root);
        assertEquals("true", root.toString());

        JSONValueBoolean bool = (JSONValueBoolean) root;
        assertTrue(bool.value());

    }

    @Test
    public void testFalseOnly() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString("false").parse();

        assertEquals(JSONValueType.BOOLEAN, root.type());
        assertTrue(root instanceof JSONValueBoolean);
        assertEquals(JSONValueBoolean.FALSE, root);
        assertEquals("false", root.toString());

        JSONValueBoolean bool = (JSONValueBoolean) root;
        assertFalse(bool.value());
    }

    @Test
    public void testStringOnly() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" \"abc\"").parse();

        assertEquals(JSONValueType.STRING, root.type());
        assertTrue(root instanceof JSONValueString);
        assertEquals(new JSONValueString("abc"), root);
        assertEquals("abc", root.toString());

        JSONValueString str = (JSONValueString) root;
        assertEquals("abc", str.value());
    }

    @Test
    public void testNumberOnly00() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" -15.234e2 ").parse();

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
        JSONValue root = JSONText.fromString("523 ").parse();

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
        JSONValue root = JSONText.fromString("-124 ").parse();

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
        JSONValue root = JSONText.fromString(" 928.5").parse();

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
        JSONValue root = JSONText.fromString(" -872.512").parse();

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

        JSONParserException ex = assertThrows(JSONParserException.class, jsText::parse);
        assertEquals(jsText, ex.source());
        assertEquals(2, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(3, ex.location().column());
    }

    @Test
    public void testNumberOnly06() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString(" -0.015   ").parse();

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
        JSONValue root = JSONText.fromString("   0.987").parse();

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
        JSONValue root = JSONText.fromString("1e6").parse();

        assertEquals(JSONValueType.NUMBER, root.type());
        assertTrue(root instanceof JSONValueNumber);
        assertEquals(new JSONValueNumber("1e6"), root);
        assertEquals("1e6", root.toString());

        JSONValueNumber num = (JSONValueNumber) root;
        assertFalse(num.canBeLong());
        assertEquals(1e6, num.toDouble());
    }

    @Test
    public void testNumberOnly09() throws IOException, JSONParserException {
        JSONValue root = JSONText.fromString("1.24e-12").parse();

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
        JSONValue root = JSONText.fromString("-5.2E+2").parse();

        assertEquals(JSONValueType.NUMBER, root.type());
        assertTrue(root instanceof JSONValueNumber);
        assertEquals(new JSONValueNumber("-5.2E+2"), root);
        assertEquals("-5.2E+2", root.toString());

        JSONValueNumber num = (JSONValueNumber) root;
        assertFalse(num.canBeLong());
        assertEquals(-5.2e2, num.toDouble());
    }

    @Test
    public void testSimpleArray() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString(" [ true, false, \"abc\", 1.52, null ] ");
        JSONValue root = jsText.parse();

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
            }

            i++;
        }
    }

    @Test
    public void testSimpleObject() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString(
                " { \"key1\": true, " +
                "   \"key2\": false," +
                "   \"key3\": null } ");
        JSONValue root = jsText.parse();

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
        for (Map.Entry<JSONValueString, JSONValue> entry: rootObj.entrySet()) {
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
            }
            i++;
        }
    }
}
