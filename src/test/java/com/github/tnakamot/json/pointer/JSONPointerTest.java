package com.github.tnakamot.json.pointer;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tnakamot.json.value.JSONValueString;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONPointerTest {
  private static final Logger log = LoggerFactory.getLogger(JSONPointerTest.class);

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
      assertEquals(2 + offset, p.tokens()[0].endLocation());
      assertEquals("x", p.tokens()[0].text());
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
      assertEquals(2 + offset, p.tokens()[0].endLocation());
      assertEquals("x", p.tokens()[0].text());
      assertEquals("x", p.tokens()[0].name());
      assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());

      assertEquals(3 + offset, p.tokens()[1].beginningLocation());
      assertEquals(3 + offset, p.tokens()[1].endLocation());
      assertEquals("", p.tokens()[1].text());
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
      assertEquals(4 + offset, p.tokens()[0].endLocation());
      assertEquals("abc", p.tokens()[0].text());
      assertEquals("abc", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(5 + offset, p.tokens()[1].beginningLocation());
      assertEquals(8 + offset, p.tokens()[1].endLocation());
      assertEquals("def", p.tokens()[1].text());
      assertEquals("def", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);

      assertEquals(9 + offset, p.tokens()[2].beginningLocation());
      assertEquals(11 + offset, p.tokens()[2].endLocation());
      assertEquals("19", p.tokens()[2].text());
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
      assertEquals(5 + offset, p.tokens()[0].endLocation());
      assertEquals("test", p.tokens()[0].text());
      assertEquals("test", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(6 + offset, p.tokens()[1].beginningLocation());
      assertEquals(6 + offset, p.tokens()[1].endLocation());
      assertEquals("", p.tokens()[1].text());
      assertEquals("", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);

      assertEquals(7 + offset, p.tokens()[2].beginningLocation());
      assertEquals(9 + offset, p.tokens()[2].endLocation());
      assertEquals("11", p.tokens()[2].text());
      assertEquals("11", p.tokens()[2].name());
      assertEquals(11, p.tokens()[2].index());
    }
  }

  @Test
  public void testPointerValidSyntax6()
      throws InvalidJSONPointerSyntaxException, InvalidJSONPointerNotIndexException {
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
      assertEquals(5 + offset, p.tokens()[0].endLocation());
      assertEquals("a~1c", p.tokens()[0].text());
      assertEquals("a/c", p.tokens()[0].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[0].index());
      log.info(ex::getMessage);

      assertEquals(6 + offset, p.tokens()[1].beginningLocation());
      assertEquals(10 + offset, p.tokens()[1].endLocation());
      assertEquals("d~0f", p.tokens()[1].text());
      assertEquals("d~f", p.tokens()[1].name());
      ex = assertThrows(InvalidJSONPointerNotIndexException.class, () -> p.tokens()[1].index());
      log.info(ex::getMessage);
    }
  }

  @Test
  public void testPointerInvalidSyntax() {
    InvalidJSONPointerSyntaxException ex;

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("x"));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#"));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/~2ab"));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/ab~"));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#x", true));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("x", true));
    log.info(ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/", true));
    log.info(ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/~2ab", true));
    log.info(ex::getMessage);

    ex =
        assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/ab~", true));
    log.info(ex::getMessage);
  }
}