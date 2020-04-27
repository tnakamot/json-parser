package com.github.tnakamoto.jscdg.lexer;

import static org.junit.Assert.*;

import com.github.tnakamot.jscdg.lexer.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class JSONLexerTest {
    @Test
    public void testEmpty() throws IOException, JSONLexerException {
        List<JSONToken> tokens = JSONText.fromString("").tokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testWSOnly() throws IOException, JSONLexerException {
        List<JSONToken> tokens = JSONText.fromString(" \r\n\t").tokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testSimpleObject() throws IOException, JSONLexerException {
        // TODO: add number
        JSONText jsText = JSONText.fromString(" { \"key\":\r\n[true,\nfalse,\rnull, -15.234e2]\n\r} ");
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.BEGIN_OBJECT, tokens.get(0).type());
        assertEquals("{", tokens.get(0).text());
        assertEquals(1, tokens.get(0).location().position());
        assertEquals(1, tokens.get(0).location().line());
        assertEquals(2, tokens.get(0).location().column());
        assertEquals(jsText, tokens.get(0).source());

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\"key\"", tokens.get(1).text());
        assertEquals(3, tokens.get(1).location().position());
        assertEquals(1, tokens.get(1).location().line());
        assertEquals(4, tokens.get(1).location().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals("key", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.NAME_SEPARATOR, tokens.get(2).type());
        assertEquals(":", tokens.get(2).text());
        assertEquals(8, tokens.get(2).location().position());
        assertEquals(1, tokens.get(2).location().line());
        assertEquals(9, tokens.get(2).location().column());
        assertEquals(jsText, tokens.get(2).source());

        assertEquals(JSONTokenType.BEGIN_ARRAY, tokens.get(3).type());
        assertEquals("[", tokens.get(3).text());
        assertEquals(11, tokens.get(3).location().position());
        assertEquals(2, tokens.get(3).location().line());
        assertEquals(1, tokens.get(3).location().column());
        assertEquals(jsText, tokens.get(3).source());

        assertEquals(JSONTokenType.BOOLEAN, tokens.get(4).type());
        assertEquals("true", tokens.get(4).text());
        assertEquals(12, tokens.get(4).location().position());
        assertEquals(2, tokens.get(4).location().line());
        assertEquals(2, tokens.get(4).location().column());
        assertEquals(jsText, tokens.get(4).source());
        assertTrue(tokens.get(4) instanceof JSONTokenBoolean);
        assertTrue(((JSONTokenBoolean) tokens.get(4)).value());

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(5).type());
        assertEquals(",", tokens.get(5).text());
        assertEquals(16, tokens.get(5).location().position());
        assertEquals(2, tokens.get(5).location().line());
        assertEquals(6, tokens.get(5).location().column());
        assertEquals(jsText, tokens.get(5).source());

        assertEquals(JSONTokenType.BOOLEAN, tokens.get(6).type());
        assertEquals("false", tokens.get(6).text());
        assertEquals(18, tokens.get(6).location().position());
        assertEquals(3, tokens.get(6).location().line());
        assertEquals(1, tokens.get(6).location().column());
        assertEquals(jsText, tokens.get(6).source());
        assertTrue(tokens.get(6) instanceof JSONTokenBoolean);
        assertFalse(((JSONTokenBoolean) tokens.get(6)).value());

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(7).type());
        assertEquals(",", tokens.get(7).text());
        assertEquals(23, tokens.get(7).location().position());
        assertEquals(3, tokens.get(7).location().line());
        assertEquals(6, tokens.get(7).location().column());
        assertEquals(jsText, tokens.get(7).source());

        assertEquals(JSONTokenType.NULL, tokens.get(8).type());
        assertEquals("null", tokens.get(8).text());
        assertEquals(25, tokens.get(8).location().position());
        assertEquals(4, tokens.get(8).location().line());
        assertEquals(1, tokens.get(8).location().column());
        assertEquals(jsText, tokens.get(8).source());
        assertTrue(tokens.get(8) instanceof JSONTokenNull);

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(9).type());
        assertEquals(",", tokens.get(9).text());
        assertEquals(29, tokens.get(9).location().position());
        assertEquals(4, tokens.get(9).location().line());
        assertEquals(5, tokens.get(9).location().column());
        assertEquals(jsText, tokens.get(9).source());

        assertEquals(JSONTokenType.NUMBER, tokens.get(10).type());
        assertEquals("-15.234e2", tokens.get(10).text());
        assertEquals(31, tokens.get(10).location().position());
        assertEquals(4, tokens.get(10).location().line());
        assertEquals(7, tokens.get(10).location().column());
        assertTrue(tokens.get(10) instanceof JSONTokenNumber);
        assertEquals(-15.234e2, ((JSONTokenNumber)tokens.get(10)).toDouble(), 0);
        assertEquals(jsText, tokens.get(10).source());

        assertEquals(JSONTokenType.END_ARRAY, tokens.get(11).type());
        assertEquals("]", tokens.get(11).text());
        assertEquals(40, tokens.get(11).location().position());
        assertEquals(4, tokens.get(11).location().line());
        assertEquals(16, tokens.get(11).location().column());
        assertEquals(jsText, tokens.get(11).source());

        assertEquals(JSONTokenType.END_OBJECT, tokens.get(12).type());
        assertEquals("}", tokens.get(12).text());
        assertEquals(43, tokens.get(12).location().position());
        assertEquals(6, tokens.get(12).location().line());
        assertEquals(1, tokens.get(12).location().column());
        assertEquals(jsText, tokens.get(12).source());
    }

    @Test
    public void testUnknownToken1() {
        JSONText jsText = JSONText.fromString("( )");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(0, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(1, ex.location().column());
    }

    @Test
    public void testUnknownToken2() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n True\n}");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(10, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(2, ex.location().column());
    }

    @Test
    public void testUnknownToken3() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n trUe\n}");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(10, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(2, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF1() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n tr");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(12, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(4, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF2() {
        JSONText jsText = JSONText.fromString("{\n\"ke");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(5, ex.location().position());
        assertEquals(2, ex.location().line());
        assertEquals(4, ex.location().column());
    }

    @Test
    public void testControlCharacterInString() {
        JSONText jsText = JSONText.fromString("{ \"key\": \"hello\nworld\" }");

        JSONLexerException ex = assertThrows(
                JSONLexerException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(15, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(16, ex.location().column());
    }

    @Test
    public void testEscapedString1() throws IOException, JSONLexerException {
        JSONText jsText = JSONText.fromString(
                "{ \"\\u006b\\u0065\\u0079\": \"\\\"test\\\"\" } "
        );
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\"\\u006b\\u0065\\u0079\"", tokens.get(1).text());
        assertEquals(2, tokens.get(1).location().position());
        assertEquals(1, tokens.get(1).location().line());
        assertEquals(3, tokens.get(1).location().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals("key", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.STRING, tokens.get(3).type());
        assertEquals("\"\\\"test\\\"\"", tokens.get(3).text());
        assertEquals(24, tokens.get(3).location().position());
        assertEquals(1, tokens.get(3).location().line());
        assertEquals(25, tokens.get(3).location().column());
        assertEquals(jsText, tokens.get(3).source());
        assertTrue(tokens.get(3) instanceof JSONTokenString);
        assertEquals("\"test\"", ((JSONTokenString) tokens.get(3)).value());
    }

    @Test
    public void testEscapedString2() throws IOException, JSONLexerException {
        JSONText jsText = JSONText.fromString(
                "{ \" \\\\ \\/path\\/test \": \"abc\\b\\f\\n\\r\\txyz\" } "
        );
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\" \\\\ \\/path\\/test \"", tokens.get(1).text());
        assertEquals(2, tokens.get(1).location().position());
        assertEquals(1, tokens.get(1).location().line());
        assertEquals(3, tokens.get(1).location().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals(" \\ /path/test ", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.STRING, tokens.get(3).type());
        assertEquals("\"abc\\b\\f\\n\\r\\txyz\"", tokens.get(3).text());
        assertEquals(23, tokens.get(3).location().position());
        assertEquals(1, tokens.get(3).location().line());
        assertEquals(24, tokens.get(3).location().column());
        assertEquals(jsText, tokens.get(3).source());
        assertTrue(tokens.get(3) instanceof JSONTokenString);
        assertEquals("abc\b\f\n\r\txyz", ((JSONTokenString) tokens.get(3)).value());
    }
}