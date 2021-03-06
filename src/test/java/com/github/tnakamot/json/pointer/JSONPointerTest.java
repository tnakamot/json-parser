package com.github.tnakamot.json.pointer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import com.github.tnakamot.json.value.JSONValue;
import com.github.tnakamot.json.value.JSONValueArray;
import com.github.tnakamot.json.value.JSONValueBoolean;
import com.github.tnakamot.json.value.JSONValueNull;
import com.github.tnakamot.json.value.JSONValueNumber;
import com.github.tnakamot.json.value.JSONValueString;
import com.github.tnakamot.json.value.JSONValueType;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONPointerTest {
  private static final Logger log = LoggerFactory.getLogger(JSONPointerTest.class);
  private static final String rfc6901Example = "/com/github/tnakamot/json/rfc6901_example.json";

  @Test
  public void testPointerEmpty() throws InvalidJSONPointerSyntaxException {
    assertEquals(0, new JSONPointer("").tokens().length);
    assertEquals(0, new JSONPointer("", false).tokens().length);
    assertEquals(0, new JSONPointer("#", true).tokens().length);
    assertEquals(0, new JSONPointer(new JSONValueString("")).tokens().length);
    assertEquals(0, new JSONPointer(new JSONValueString(""), false).tokens().length);
    assertEquals(0, new JSONPointer(new JSONValueString("#"), true).tokens().length);
  }

  @Test
  public void testPointerValidSyntax1() throws InvalidJSONPointerSyntaxException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/"),
          new JSONPointer("/", false),
          new JSONPointer(new JSONValueString("/")),
          new JSONPointer(new JSONValueString("/")),
          new JSONPointer("#/", true),
          new JSONPointer(new JSONValueString("#/"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(1, p.tokens().length);
      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(1 + offset, p.tokens()[0].endLocation());
      assertEquals("", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);
    }
  }

  @Test
  public void testPointerValidSyntax2() throws InvalidJSONPointerSyntaxException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/x"),
          new JSONPointer("/x", false),
          new JSONPointer(new JSONValueString("/x")),
          new JSONPointer(new JSONValueString("/x")),
          new JSONPointer("#/x", true),
          new JSONPointer(new JSONValueString("#/x"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(1, p.tokens().length);
      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(1 + offset, p.tokens()[0].endLocation());
      assertEquals("x", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("x", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);
    }
  }

  @Test
  public void testPointerValidSyntax3() throws InvalidJSONPointerSyntaxException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/x/"),
          new JSONPointer("/x/", false),
          new JSONPointer(new JSONValueString("/x/")),
          new JSONPointer(new JSONValueString("/x/")),
          new JSONPointer("#/x/", true),
          new JSONPointer(new JSONValueString("#/x/"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(2, p.tokens().length);

      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(1 + offset, p.tokens()[0].endLocation());
      assertEquals("x", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("x", p.tokens()[0].name());
      assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());

      assertEquals(3 + offset, p.tokens()[1].beginningLocation());
      assertEquals(3 + offset, p.tokens()[1].endLocation());
      assertEquals("", p.tokens()[1].text());
      assertEquals("/x", p.tokens()[1].parent());
      assertEquals("", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);
    }
  }

  @Test
  public void testPointerValidSyntax4()
      throws InvalidJSONPointerSyntaxException, InvalidJSONPointerNotIndexException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/abc/def/19"),
          new JSONPointer("/abc/def/19", false),
          new JSONPointer(new JSONValueString("/abc/def/19")),
          new JSONPointer(new JSONValueString("/abc/def/19")),
          new JSONPointer("#/abc/def/19", true),
          new JSONPointer(new JSONValueString("#/abc/def/19"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(3, p.tokens().length);

      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(3 + offset, p.tokens()[0].endLocation());
      assertEquals("abc", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("abc", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(5 + offset, p.tokens()[1].beginningLocation());
      assertEquals(7 + offset, p.tokens()[1].endLocation());
      assertEquals("def", p.tokens()[1].text());
      assertEquals("/abc", p.tokens()[1].parent());
      assertEquals("def", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);

      assertEquals(9 + offset, p.tokens()[2].beginningLocation());
      assertEquals(10 + offset, p.tokens()[2].endLocation());
      assertEquals("19", p.tokens()[2].text());
      assertEquals("/abc/def", p.tokens()[2].parent());
      assertEquals("19", p.tokens()[2].name());
      assertEquals(19, p.tokens()[2].index());
    }
  }

  @Test
  public void testPointerValidSyntax5()
      throws InvalidJSONPointerSyntaxException, InvalidJSONPointerNotIndexException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/test//11"),
          new JSONPointer("/test//11", false),
          new JSONPointer(new JSONValueString("/test//11")),
          new JSONPointer(new JSONValueString("/test//11")),
          new JSONPointer("#/test//11", true),
          new JSONPointer(new JSONValueString("#/test//11"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(3, p.tokens().length);

      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(4 + offset, p.tokens()[0].endLocation());
      assertEquals("test", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("test", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(6 + offset, p.tokens()[1].beginningLocation());
      assertEquals(6 + offset, p.tokens()[1].endLocation());
      assertEquals("", p.tokens()[1].text());
      assertEquals("/test", p.tokens()[1].parent());
      assertEquals("", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);

      assertEquals(7 + offset, p.tokens()[2].beginningLocation());
      assertEquals(8 + offset, p.tokens()[2].endLocation());
      assertEquals("11", p.tokens()[2].text());
      assertEquals("/test/", p.tokens()[2].parent());
      assertEquals("11", p.tokens()[2].name());
      assertEquals(11, p.tokens()[2].index());
    }
  }

  @Test
  public void testPointerValidSyntax6() throws InvalidJSONPointerSyntaxException {
    InvalidJSONPointerException ex;
    JSONPointer[] ps =
        new JSONPointer[] {
          new JSONPointer("/a~1c/d~0f"),
          new JSONPointer("/a~1c/d~0f", false),
          new JSONPointer(new JSONValueString("/a~1c/d~0f")),
          new JSONPointer(new JSONValueString("/a~1c/d~0f")),
          new JSONPointer("#/a~1c/d~0f", true),
          new JSONPointer(new JSONValueString("#/a~1c/d~0f"), true),
        };

    for (JSONPointer p : ps) {
      int offset = p.text().startsWith("#") ? 1 : 0;
      assertEquals(2, p.tokens().length);

      assertEquals(1 + offset, p.tokens()[0].beginningLocation());
      assertEquals(4 + offset, p.tokens()[0].endLocation());
      assertEquals("a~1c", p.tokens()[0].text());
      assertEquals("", p.tokens()[0].parent());
      assertEquals("a/c", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(6 + offset, p.tokens()[1].beginningLocation());
      assertEquals(9 + offset, p.tokens()[1].endLocation());
      assertEquals("d~0f", p.tokens()[1].text());
      assertEquals("/a~1c", p.tokens()[1].parent());
      assertEquals("d~f", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);
    }
  }

  @Test
  public void testPointerInvalidSyntax() {
    InvalidJSONPointerSyntaxException ex;

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("x"));
    assertEquals("x", ex.text());
    assertEquals(0, ex.begin());
    assertEquals(0, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#"));
    assertEquals("#", ex.text());
    assertEquals(0, ex.begin());
    assertEquals(0, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/~2ab"));
    assertEquals("/~2ab", ex.text());
    assertEquals(2, ex.begin());
    assertEquals(2, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/ab~"));
    assertEquals("/ab~", ex.text());
    assertEquals(4, ex.begin());
    assertEquals(4, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#x", true));
    assertEquals("#x", ex.text());
    assertEquals(1, ex.begin());
    assertEquals(1, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("x", true));
    assertEquals("x", ex.text());
    assertEquals(0, ex.begin());
    assertEquals(0, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/", true));
    assertEquals("/", ex.text());
    assertEquals(0, ex.begin());
    assertEquals(0, ex.end());
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/~2ab", true));
    assertEquals("#/~2ab", ex.text());
    assertEquals(3, ex.begin());
    assertEquals(3, ex.end());
    log.info(ex::getMessage);

    ex =
        assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/ab~", true));
    assertEquals("#/ab~", ex.text());
    assertEquals(5, ex.begin());
    assertEquals(5, ex.end());
    log.info(ex::getMessage);
  }

  @Test
  public void testRFC6901Example()
      throws InvalidJSONPointerException, IOException, JSONParserException, URISyntaxException {
    JSONText jsText = JSONText.fromURL(this.getClass().getResource(rfc6901Example));
    JSONValue root = jsText.parse().root();

    assertEquals(root, jsText.evaluate(""));

    assertEquals(JSONValueType.ARRAY, jsText.evaluate("/foo").type());
    JSONValueArray array = (JSONValueArray) jsText.evaluate("/foo");
    assertEquals(2, array.size());
    assertEquals("bar", array.getString(0));
    assertEquals("baz", array.getString(1));

    assertEquals(new JSONValueString("bar"), jsText.evaluate("/foo/0"));
    assertEquals(new JSONValueNumber(0), jsText.evaluate("/"));
    assertEquals(new JSONValueNumber(1), jsText.evaluate("/a~1b"));
    assertEquals(new JSONValueNumber(2), jsText.evaluate("/c%d"));
    assertEquals(new JSONValueNumber(3), jsText.evaluate("/e^f"));
    assertEquals(new JSONValueNumber(4), jsText.evaluate("/g|h"));
    assertEquals(new JSONValueNumber(5), jsText.evaluate("/i\\j"));
    assertEquals(new JSONValueNumber(6), jsText.evaluate("/k\"l"));
    assertEquals(new JSONValueNumber(7), jsText.evaluate("/ "));
    assertEquals(new JSONValueNumber(8), jsText.evaluate("/m~0n"));

    InvalidJSONPointerException ex;
    ex =
        assertThrows(
            InvalidJSONPointerMemberNotExistException.class, () -> jsText.evaluate("/abc"));
    assertEquals("/abc", ex.text());
    assertEquals(1, ex.begin());
    assertEquals(3, ex.end());
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerIndexOutOfBoundsException.class, () -> jsText.evaluate("/foo/2"));
    assertEquals("/foo/2", ex.text());
    assertEquals(5, ex.begin());
    assertEquals(5, ex.end());
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> jsText.evaluate("/foo/a"));
    assertEquals("/foo/a", ex.text());
    assertEquals(5, ex.begin());
    assertEquals(5, ex.end());
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerReachedPrimitiveException.class, () -> jsText.evaluate("//abc"));
    assertEquals("//abc", ex.text());
    assertEquals(2, ex.begin());
    assertEquals(4, ex.end());
    log.info(ex::getMessage);
  }

  @Test
  public void testInvalidJSONPointerForRootArray()
      throws InvalidJSONPointerException, IOException, JSONParserException {
    JSONText jsText = JSONText.fromString("[5, true, null, {\"key1\": 3.15}]");
    assertFalse(jsText.isParsed());
    assertThrows(IllegalStateException.class, () -> jsText.evaluate("/0"));

    JSONValue root = jsText.parse().root();
    assertTrue(jsText.isParsed());

    assertEquals(root, jsText.evaluate(""));

    assertEquals(new JSONValueNumber(5), jsText.evaluate("/0"));
    assertEquals(JSONValueBoolean.TRUE, jsText.evaluate("/1"));
    assertEquals(JSONValueNull.INSTANCE, jsText.evaluate("/2"));
    assertEquals(JSONValueType.OBJECT, jsText.evaluate("/3").type());
    assertEquals(new JSONValueNumber(3.15), jsText.evaluate("/3/key1"));

    InvalidJSONPointerWithTokenException ex;
    ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> jsText.evaluate("/abc"));
    assertEquals("/abc", ex.text());
    assertEquals(1, ex.begin());
    assertEquals(3, ex.end());
    assertEquals("abc", ex.token().text());
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerIndexOutOfBoundsException.class, () -> jsText.evaluate("/4"));
    assertEquals("/4", ex.text());
    assertEquals(1, ex.begin());
    assertEquals(1, ex.end());
    assertEquals("4", ex.token().text());
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerReachedPrimitiveException.class, () -> jsText.evaluate("/0/1"));
    assertEquals("/0/1", ex.text());
    assertEquals(3, ex.begin());
    assertEquals(3, ex.end());
    assertEquals("1", ex.token().text());
    log.info(ex::getMessage);

    ex =
        assertThrows(InvalidJSONPointerMemberNotExistException.class, () -> jsText.evaluate("/3/"));
    assertEquals("/3/", ex.text());
    assertEquals(3, ex.begin());
    assertEquals(3, ex.end());
    assertEquals("", ex.token().text());
    log.info(ex::getMessage);
  }

  @Test
  public void testExample() throws InvalidJSONPointerException, IOException, JSONParserException {
    JSONText jsText =
        JSONText.fromString(
            "{"
                + "  \"key1\": \"value\", "
                + "  \"key2\": [3, 1, 4], "
                + "  \"key3\": true,"
                + "  \"key4\": {\"key5\": \"hello!\" }"
                + "} ");
    jsText.parse();

    JSONValueString val1 = (JSONValueString) jsText.evaluate("/key1");
    assertEquals("value", val1.value());
    System.out.println(val1.value());

    JSONValueNumber val2 = (JSONValueNumber) jsText.evaluate("/key2/1");
    assertEquals(1, val2.toLong());
    System.out.println(val2.toLong());

    JSONValueString val3 = (JSONValueString) jsText.evaluate("/key4/key5");
    assertEquals("hello!", val3.value());
    System.out.println(val3.value());
  }
}
