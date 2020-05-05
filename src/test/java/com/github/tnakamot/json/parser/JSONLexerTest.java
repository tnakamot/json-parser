/*
 *  Copyright (C) 2020 Takashi Nakamoto <nyakamoto@gmail.com>.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 3 as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.tnakamot.json.parser;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.token.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class JSONLexerTest {
    @Test
    public void testEmpty() throws IOException, JSONParserException {
        List<JSONToken> tokens = JSONText.fromString("").tokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testWSOnly() throws IOException, JSONParserException {
        List<JSONToken> tokens = JSONText.fromString(" \r\n\t").tokens();
        assertEquals(0, tokens.size());
    }

    @Test
    public void testSimpleObject() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString(" { \"key\":\r\n[true,\nfalse,\rnull, -15.234e2\r\n]\n\r} ");
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.BEGIN_OBJECT, tokens.get(0).type());
        assertEquals("{", tokens.get(0).text());
        assertEquals(1, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(2, tokens.get(0).beginningLocation().column());
        assertEquals(2, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(3, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\"key\"", tokens.get(1).text());
        assertEquals(3, tokens.get(1).beginningLocation().position());
        assertEquals(1, tokens.get(1).beginningLocation().line());
        assertEquals(4, tokens.get(1).beginningLocation().column());
        assertEquals(8, tokens.get(1).endLocation().position());
        assertEquals(1, tokens.get(1).endLocation().line());
        assertEquals(9, tokens.get(1).endLocation().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals("key", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.NAME_SEPARATOR, tokens.get(2).type());
        assertEquals(":", tokens.get(2).text());
        assertEquals(8, tokens.get(2).beginningLocation().position());
        assertEquals(1, tokens.get(2).beginningLocation().line());
        assertEquals(9, tokens.get(2).beginningLocation().column());
        assertEquals(9, tokens.get(2).endLocation().position());
        assertEquals(1, tokens.get(2).endLocation().line());
        assertEquals(10, tokens.get(2).endLocation().column());
        assertEquals(jsText, tokens.get(2).source());

        assertEquals(JSONTokenType.BEGIN_ARRAY, tokens.get(3).type());
        assertEquals("[", tokens.get(3).text());
        assertEquals(11, tokens.get(3).beginningLocation().position());
        assertEquals(2, tokens.get(3).beginningLocation().line());
        assertEquals(1, tokens.get(3).beginningLocation().column());
        assertEquals(12, tokens.get(3).endLocation().position());
        assertEquals(2, tokens.get(3).endLocation().line());
        assertEquals(2, tokens.get(3).endLocation().column());
        assertEquals(jsText, tokens.get(3).source());

        assertEquals(JSONTokenType.BOOLEAN, tokens.get(4).type());
        assertEquals("true", tokens.get(4).text());
        assertEquals(12, tokens.get(4).beginningLocation().position());
        assertEquals(2, tokens.get(4).beginningLocation().line());
        assertEquals(2, tokens.get(4).beginningLocation().column());
        assertEquals(16, tokens.get(4).endLocation().position());
        assertEquals(2, tokens.get(4).endLocation().line());
        assertEquals(6, tokens.get(4).endLocation().column());
        assertEquals(jsText, tokens.get(4).source());
        assertTrue(tokens.get(4) instanceof JSONTokenBoolean);
        assertTrue(((JSONTokenBoolean) tokens.get(4)).value());

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(5).type());
        assertEquals(",", tokens.get(5).text());
        assertEquals(16, tokens.get(5).beginningLocation().position());
        assertEquals(2, tokens.get(5).beginningLocation().line());
        assertEquals(6, tokens.get(5).beginningLocation().column());
        assertEquals(17, tokens.get(5).endLocation().position());
        assertEquals(2, tokens.get(5).endLocation().line());
        assertEquals(7, tokens.get(5).endLocation().column());
        assertEquals(jsText, tokens.get(5).source());

        assertEquals(JSONTokenType.BOOLEAN, tokens.get(6).type());
        assertEquals("false", tokens.get(6).text());
        assertEquals(18, tokens.get(6).beginningLocation().position());
        assertEquals(3, tokens.get(6).beginningLocation().line());
        assertEquals(1, tokens.get(6).beginningLocation().column());
        assertEquals(23, tokens.get(6).endLocation().position());
        assertEquals(3, tokens.get(6).endLocation().line());
        assertEquals(6, tokens.get(6).endLocation().column());
        assertEquals(jsText, tokens.get(6).source());
        assertTrue(tokens.get(6) instanceof JSONTokenBoolean);
        assertFalse(((JSONTokenBoolean) tokens.get(6)).value());

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(7).type());
        assertEquals(",", tokens.get(7).text());
        assertEquals(23, tokens.get(7).beginningLocation().position());
        assertEquals(3, tokens.get(7).beginningLocation().line());
        assertEquals(6, tokens.get(7).beginningLocation().column());
        assertEquals(24, tokens.get(7).endLocation().position());
        assertEquals(3, tokens.get(7).endLocation().line());
        assertEquals(7, tokens.get(7).endLocation().column());
        assertEquals(jsText, tokens.get(7).source());

        assertEquals(JSONTokenType.NULL, tokens.get(8).type());
        assertEquals("null", tokens.get(8).text());
        assertEquals(25, tokens.get(8).beginningLocation().position());
        assertEquals(4, tokens.get(8).beginningLocation().line());
        assertEquals(1, tokens.get(8).beginningLocation().column());
        assertEquals(29, tokens.get(8).endLocation().position());
        assertEquals(4, tokens.get(8).endLocation().line());
        assertEquals(5, tokens.get(8).endLocation().column());
        assertEquals(jsText, tokens.get(8).source());
        assertTrue(tokens.get(8) instanceof JSONTokenNull);

        assertEquals(JSONTokenType.VALUE_SEPARATOR, tokens.get(9).type());
        assertEquals(",", tokens.get(9).text());
        assertEquals(29, tokens.get(9).beginningLocation().position());
        assertEquals(4, tokens.get(9).beginningLocation().line());
        assertEquals(5, tokens.get(9).beginningLocation().column());
        assertEquals(30, tokens.get(9).endLocation().position());
        assertEquals(4, tokens.get(9).endLocation().line());
        assertEquals(6, tokens.get(9).endLocation().column());
        assertEquals(jsText, tokens.get(9).source());

        assertEquals(JSONTokenType.NUMBER, tokens.get(10).type());
        assertEquals("-15.234e2", tokens.get(10).text());
        assertEquals(31, tokens.get(10).beginningLocation().position());
        assertEquals(4, tokens.get(10).beginningLocation().line());
        assertEquals(7, tokens.get(10).beginningLocation().column());
        assertEquals(40, tokens.get(10).endLocation().position());
        assertEquals(4, tokens.get(10).endLocation().line());
        assertEquals(16, tokens.get(10).endLocation().column());
        assertTrue(tokens.get(10) instanceof JSONTokenNumber);
        assertEquals(jsText, tokens.get(10).source());

        assertEquals(JSONTokenType.END_ARRAY, tokens.get(11).type());
        assertEquals("]", tokens.get(11).text());
        assertEquals(42, tokens.get(11).beginningLocation().position());
        assertEquals(5, tokens.get(11).beginningLocation().line());
        assertEquals(1, tokens.get(11).beginningLocation().column());
        assertEquals(43, tokens.get(11).endLocation().position());
        assertEquals(5, tokens.get(11).endLocation().line());
        assertEquals(2, tokens.get(11).endLocation().column());
        assertEquals(jsText, tokens.get(11).source());

        assertEquals(JSONTokenType.END_OBJECT, tokens.get(12).type());
        assertEquals("}", tokens.get(12).text());
        assertEquals(45, tokens.get(12).beginningLocation().position());
        assertEquals(7, tokens.get(12).beginningLocation().line());
        assertEquals(1, tokens.get(12).beginningLocation().column());
        assertEquals(46, tokens.get(12).endLocation().position());
        assertEquals(7, tokens.get(12).endLocation().line());
        assertEquals(2, tokens.get(12).endLocation().column());
        assertEquals(jsText, tokens.get(12).source());
    }

    @Test
    public void testUnknownToken1() {
        JSONText jsText = JSONText.fromString("( )");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(0, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(1, ex.location().column());
    }

    @Test
    public void testUnknownToken2() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n True\n}");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(10, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(2, ex.location().column());
    }

    @Test
    public void testUnknownToken3() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n trUe\n}");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(10, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(2, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF01() {
        JSONText jsText = JSONText.fromString("{\n\"key\":\n tr");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(12, ex.location().position());
        assertEquals(3, ex.location().line());
        assertEquals(4, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF02() {
        JSONText jsText = JSONText.fromString("{\n\"ke");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(5, ex.location().position());
        assertEquals(2, ex.location().line());
        assertEquals(4, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF03() {
        JSONText jsText = JSONText.fromString("\"key");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(4, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(5, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF04() {
        JSONText jsText = JSONText.fromString("123.");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(4, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(5, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF05() {
        JSONText jsText = JSONText.fromString("-");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(1, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(2, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF06() {
        JSONText jsText = JSONText.fromString("-151.");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(5, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(6, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF07() {
        JSONText jsText = JSONText.fromString("-151.5e");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(7, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(8, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF08() {
        JSONText jsText = JSONText.fromString("-151.5E");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(7, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(8, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF09() {
        JSONText jsText = JSONText.fromString("-151.5E-");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(8, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(9, ex.location().column());
    }

    @Test
    public void testUnexpectedEOF10() {
        JSONText jsText = JSONText.fromString("-151.5E+");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(8, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(9, ex.location().column());
    }

    @Test
    public void testControlCharacterInString() {
        JSONText jsText = JSONText.fromString("{ \"key\": \"hello\nworld\" }");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(15, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(16, ex.location().column());
    }

    @Test
    public void testEscapedString1() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString(
                "{ \"\\u006b\\u0065\\u0079\": \"\\\"test\\\"\" } "
        );
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\"\\u006b\\u0065\\u0079\"", tokens.get(1).text());
        assertEquals(2, tokens.get(1).beginningLocation().position());
        assertEquals(1, tokens.get(1).beginningLocation().line());
        assertEquals(3, tokens.get(1).beginningLocation().column());
        assertEquals(22, tokens.get(1).endLocation().position());
        assertEquals(1, tokens.get(1).endLocation().line());
        assertEquals(23, tokens.get(1).endLocation().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals("key", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.STRING, tokens.get(3).type());
        assertEquals("\"\\\"test\\\"\"", tokens.get(3).text());
        assertEquals(24, tokens.get(3).beginningLocation().position());
        assertEquals(1, tokens.get(3).beginningLocation().line());
        assertEquals(25, tokens.get(3).beginningLocation().column());
        assertEquals(34, tokens.get(3).endLocation().position());
        assertEquals(1, tokens.get(3).endLocation().line());
        assertEquals(35, tokens.get(3).endLocation().column());
        assertEquals(jsText, tokens.get(3).source());
        assertTrue(tokens.get(3) instanceof JSONTokenString);
        assertEquals("\"test\"", ((JSONTokenString) tokens.get(3)).value());
    }

    @Test
    public void testEscapedString2() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString(
                "{ \" \\\\ \\/path\\/test \": \"abc\\b\\f\\n\\r\\txyz\" } "
        );
        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.STRING, tokens.get(1).type());
        assertEquals("\" \\\\ \\/path\\/test \"", tokens.get(1).text());
        assertEquals(2, tokens.get(1).beginningLocation().position());
        assertEquals(1, tokens.get(1).beginningLocation().line());
        assertEquals(3, tokens.get(1).beginningLocation().column());
        assertEquals(21, tokens.get(1).endLocation().position());
        assertEquals(1, tokens.get(1).endLocation().line());
        assertEquals(22, tokens.get(1).endLocation().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenString);
        assertEquals(" \\ /path/test ", ((JSONTokenString) tokens.get(1)).value());

        assertEquals(JSONTokenType.STRING, tokens.get(3).type());
        assertEquals("\"abc\\b\\f\\n\\r\\txyz\"", tokens.get(3).text());
        assertEquals(23, tokens.get(3).beginningLocation().position());
        assertEquals(1, tokens.get(3).beginningLocation().line());
        assertEquals(24, tokens.get(3).beginningLocation().column());
        assertEquals(41, tokens.get(3).endLocation().position());
        assertEquals(1, tokens.get(3).endLocation().line());
        assertEquals(42, tokens.get(3).endLocation().column());
        assertEquals(jsText, tokens.get(3).source());
        assertTrue(tokens.get(3) instanceof JSONTokenString);
        assertEquals("abc\b\f\n\r\txyz", ((JSONTokenString) tokens.get(3)).value());
    }

    @Test
    public void testInvalidEscapeSequence() {
        JSONText jsText = JSONText.fromString("{ \"key\": \"he \\a llo\" }");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(14, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(15, ex.location().column());
    }

    @Test
    public void testInvalidUnicodeEscapeSequence() {
        JSONText jsText = JSONText.fromString("{ \"key\": \"he \\u1Zff llo\" }");

        JSONParserException ex = assertThrows(
                JSONParserException.class,
                jsText::tokens);
        assertEquals(jsText, ex.source());
        assertEquals(16, ex.location().position());
        assertEquals(1, ex.location().line());
        assertEquals(17, ex.location().column());
    }

    @Test
    public void testNumberToken01() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("523");

        List<JSONToken> tokens = jsText.tokens();


        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("523", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(3, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(4, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken02() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("-124");

        List<JSONToken> tokens = jsText.tokens();


        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("-124", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(4, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(5, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken03() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("928.5");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("928.5", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(5, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(6, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }


    @Test
    public void testNumberToken04() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("-872.512");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("-872.512", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(8, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(9, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken05() throws IOException, JSONParserException {
        // Because leading zero is not allowed, this text is considered
        // to have two number tokens "0" and "12". This text is not valid,
        // but such validation should occur not in a lexical analyzer, but
        // later stage.
        JSONText jsText = JSONText.fromString("012");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("0", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(1, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(2, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);

        assertEquals(JSONTokenType.NUMBER, tokens.get(1).type());
        assertEquals("12", tokens.get(1).text());
        assertEquals(1, tokens.get(1).beginningLocation().position());
        assertEquals(1, tokens.get(1).beginningLocation().line());
        assertEquals(2, tokens.get(1).beginningLocation().column());
        assertEquals(3, tokens.get(1).endLocation().position());
        assertEquals(1, tokens.get(1).endLocation().line());
        assertEquals(4, tokens.get(1).endLocation().column());
        assertEquals(jsText, tokens.get(1).source());
        assertTrue(tokens.get(1) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken06() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("-0.015");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("-0.015", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(6, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(7, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken07() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("0.987");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("0.987", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(5, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(6, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken08() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("1e6");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("1e6", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(3, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(4, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken09() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("1.24e-12");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("1.24e-12", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(8, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(9, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }

    @Test
    public void testNumberToken10() throws IOException, JSONParserException {
        JSONText jsText = JSONText.fromString("-5.2E+2");

        List<JSONToken> tokens = jsText.tokens();

        assertEquals(JSONTokenType.NUMBER, tokens.get(0).type());
        assertEquals("-5.2E+2", tokens.get(0).text());
        assertEquals(0, tokens.get(0).beginningLocation().position());
        assertEquals(1, tokens.get(0).beginningLocation().line());
        assertEquals(1, tokens.get(0).beginningLocation().column());
        assertEquals(7, tokens.get(0).endLocation().position());
        assertEquals(1, tokens.get(0).endLocation().line());
        assertEquals(8, tokens.get(0).endLocation().column());
        assertEquals(jsText, tokens.get(0).source());
        assertTrue(tokens.get(0) instanceof JSONTokenNumber);
    }
}