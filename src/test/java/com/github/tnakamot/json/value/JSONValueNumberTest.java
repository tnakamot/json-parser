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

package com.github.tnakamot.json.value;

import static org.junit.jupiter.api.Assertions.*;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

public class JSONValueNumberTest {
  private static final Logger log = LoggerFactory.getLogger(JSONValueNumberTest.class);

  @Test
  public void testNull() {
    assertThrows(NullPointerException.class, () -> new JSONValueNumber((String) null));
  }

  @ParameterizedTest(name = "testInvalidNumber: \"{0}\"")
  @ValueSource(
      strings = {
        "",
        "xyz",
        "0x1b",
        "+1",
        "--12",
        "00015",
        "15..12",
        ".512",
        "1524.",
        "152xy",
        "124,123",
        "00",
        "1.512.23",
        "124e",
        "591E",
        "151 512",
        "876e +2",
        "153.e38",
        "e13",
        "-.1243",
        "NaN",
        "Inf",
        "-Inf",
      })
  public void testInvalidNumber(String text) {
    assertThrows(NumberFormatException.class, () -> new JSONValueNumber(text));
  }

  @ParameterizedTest(name = "testDoubleNumber: \"{0}\"")
  @MethodSource("validDoubleProvider")
  public void testDoubleNumber(String text, double expected) {
    JSONValueNumber num = new JSONValueNumber(text);
    assertEquals(JSONValueType.NUMBER, num.type());
    assertEquals(text, num.text());
    assertEquals(text, num.toString());
    assertEquals(expected, num.toDouble());
  }

  static Stream<Arguments> validDoubleProvider() {
    return Stream.of(
        Arguments.of("0", 0.0),
        Arguments.of("0.0000000", 0.0),
        Arguments.of("-0", -0.0),
        Arguments.of("152", 152.0),
        Arguments.of("0.0", 0.0),
        Arguments.of("1.5", 1.5),
        Arguments.of("0.32", 0.32),
        Arguments.of("-123", -123.0),
        Arguments.of("-0.62", -0.62),
        Arguments.of("-521.3", -521.3),
        Arguments.of("1.00000", 1.0),
        Arguments.of("1e2", 1e2),
        Arguments.of("52e+2", 52e2),
        Arguments.of("88e-2", 88e-2),
        Arguments.of("-71E2", -71e2),
        Arguments.of("82E+2", 82e2),
        Arguments.of("0E-2", 0e2),
        Arguments.of("2.5e5", 2.5e5),
        Arguments.of("-3.14e+2", -3.14e2),
        Arguments.of("93.2e-22", 93.2e-22),
        Arguments.of("999.9E1", 999.9e1),
        Arguments.of("-22.5E+3", -22.5e3),
        Arguments.of("2.50e-4", 2.50e-4),
        Arguments.of("1.797693134862315807E+308", 1.797693134862315807E+308),
        Arguments.of("1.797693134862315808E+308", Double.POSITIVE_INFINITY),
        Arguments.of("0.1e309", 1E308),
        Arguments.of("1e309", Double.POSITIVE_INFINITY),
        Arguments.of("-1.797693134862315807E+308", -1.797693134862315807E+308),
        Arguments.of("-1.797693134862315808E+308", Double.NEGATIVE_INFINITY),
        Arguments.of("1e309", Double.POSITIVE_INFINITY),
        Arguments.of("-0.1e309", -1E308),
        Arguments.of("-1e309", Double.NEGATIVE_INFINITY),
        Arguments.of("4.94065645841246544E-324", 4.94065645841246544E-324),
        Arguments.of("-4.94065645841246544E-324", -4.94065645841246544E-324),
        Arguments.of("4.94065645841246544E-325", 0.0),
        Arguments.of("-4.94065645841246544E-325", -0.0),
        Arguments.of("2.5E-324", 4.94065645841246544E-324),
        Arguments.of("-2.5E-324", -4.94065645841246544E-324),
        Arguments.of("2.4E-324", 0.0),
        Arguments.of("-2.4E-324", -0.0),
        Arguments.of("1", 1.0));
  }

  @ParameterizedTest(name = "testLongNumber: \"{0}\"")
  @MethodSource("validLongProvider")
  public void testLongNumber(String text, long expected) {
    JSONValueNumber num = new JSONValueNumber(text);
    assertEquals(JSONValueType.NUMBER, num.type());
    assertEquals(text, num.text());
    assertEquals(text, num.toString());
    assertTrue(num.canBeLong());
    assertEquals(expected, num.toLong());
  }

  static Stream<Arguments> validLongProvider() {
    return Stream.of(
        Arguments.of("0", 0L),
        Arguments.of("-0", 0L),
        Arguments.of("9223372036854775807", 9223372036854775807L),
        Arguments.of("-9223372036854775808", -9223372036854775808L),
        Arguments.of("1e3", 1000L),
        Arguments.of("1.52e2", 152L),
        Arguments.of("0.000", 0L),
        Arguments.of("2.00000", 2L),
        Arguments.of("1", 1L));
  }

  @ParameterizedTest(name = "testInvalidLongNumber: \"{0}\"")
  @ValueSource(
      strings = {
        "1.52",
        "9223372036854775808",
        "-9223372036854775809",
        "1e2147483648",
        "1e-2147483649",
        "1.523e2"
      })
  public void testInvalidLongNumber(String text) {
    JSONValueNumber num = new JSONValueNumber(text);
    assertEquals(JSONValueType.NUMBER, num.type());
    assertEquals(text, num.text());
    assertEquals(text, num.toString());
    assertFalse(num.canBeLong());
    NumberFormatException ex = assertThrows(NumberFormatException.class, num::toLong);
    log.info(ex, () -> "Error message when converting '" + text + "' to long.");
  }

  @Test
  public void testEquality() throws IOException, JSONParserException {
    JSONValueNumber val1 = new JSONValueNumber("123");
    JSONValueNumber val2 = new JSONValueNumber("123");
    JSONValueNumber val3 = (JSONValueNumber) JSONText.fromString("123").parse();

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
    JSONValueNumber val1 = new JSONValueNumber("123");
    JSONValueNumber val2 = new JSONValueNumber("1.23e2");
    JSONValueNumber val3 = (JSONValueNumber) JSONText.fromString("12300E-2").parse();

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
  public void testFromDouble() {
    JSONValueNumber val = new JSONValueNumber(1.23);
    assertEquals(1.23, val.toDouble());
    assertEquals("1.23", val.toString());
    assertEquals("1.23", val.toTokenString());
  }

  @Test
  public void testFromLong() {
    JSONValueNumber val = new JSONValueNumber(512);
    assertTrue(val.canBeLong());
    assertEquals(512, val.toLong());
    assertEquals("512", val.toString());
    assertEquals("512", val.toTokenString());
  }

  @Test
  public void testNaN() {
    assertThrows(IllegalArgumentException.class, () -> new JSONValueNumber(Double.NaN));
    assertThrows(IllegalArgumentException.class, () -> new JSONValueNumber(Double.parseDouble("NaN")));
  }

  @Test
  public void testInfinite() {
    assertThrows(
        IllegalArgumentException.class, () -> new JSONValueNumber(Double.POSITIVE_INFINITY));
    assertThrows(
        IllegalArgumentException.class, () -> new JSONValueNumber(Double.parseDouble("Infinity")));
    assertThrows(
        IllegalArgumentException.class, () -> new JSONValueNumber(Double.NEGATIVE_INFINITY));
    assertThrows(
        IllegalArgumentException.class, () -> new JSONValueNumber(Double.parseDouble("-Infinity")));
  }
}
