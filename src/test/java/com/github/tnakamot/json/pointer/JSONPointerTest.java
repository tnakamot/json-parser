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
  public void testPointerValidSyntax() throws InvalidJSONPointerSyntaxException {
    assertArrayEquals(new String[0], new JSONPointer("").tokens());
    assertArrayEquals(new String[0], new JSONPointer("", false).tokens());
    assertArrayEquals(new String[0], new JSONPointer("#", true).tokens());
    assertArrayEquals(new String[0], new JSONPointer(new JSONValueString("")).tokens());
    assertArrayEquals(new String[0], new JSONPointer(new JSONValueString(""), false).tokens());
    assertArrayEquals(new String[0], new JSONPointer(new JSONValueString("#"), true).tokens());

    String[] s0 = {"x"};
    assertArrayEquals(s0, new JSONPointer("/x").tokens());
    assertArrayEquals(s0, new JSONPointer("/x", false).tokens());
    assertArrayEquals(s0, new JSONPointer("#/x", true).tokens());
    assertArrayEquals(s0, new JSONPointer(new JSONValueString("/x")).tokens());
    assertArrayEquals(s0, new JSONPointer(new JSONValueString("/x"), false).tokens());
    assertArrayEquals(s0, new JSONPointer(new JSONValueString("#/x"), true).tokens());

    String[] s1 = {"abc", "def", "19"};
    assertArrayEquals(s1, new JSONPointer("/abc/def/19").tokens());
    assertArrayEquals(s1, new JSONPointer("/abc/def/19", false).tokens());
    assertArrayEquals(s1, new JSONPointer("#/abc/def/19", true).tokens());
    assertArrayEquals(s1, new JSONPointer(new JSONValueString("/abc/def/19")).tokens());
    assertArrayEquals(s1, new JSONPointer(new JSONValueString("/abc/def/19"), false).tokens());
    assertArrayEquals(s1, new JSONPointer(new JSONValueString("#/abc/def/19"), true).tokens());

    String[] s2 = {"a/c", "d~f"};
    assertArrayEquals(s2, new JSONPointer("/a~1c/d~0f").tokens());
    assertArrayEquals(s2, new JSONPointer("/a~1c/d~0f", false).tokens());
    assertArrayEquals(s2, new JSONPointer("#/a~1c/d~0f", true).tokens());
    assertArrayEquals(s2, new JSONPointer(new JSONValueString("/a~1c/d~0f")).tokens());
    assertArrayEquals(s2, new JSONPointer(new JSONValueString("/a~1c/d~0f"), false).tokens());
    assertArrayEquals(s2, new JSONPointer(new JSONValueString("#/a~1c/d~0f"), true).tokens());
  }

  @Test
  public void testPointerInvalidSyntax() {
    InvalidJSONPointerSyntaxException ex;

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/"));
    assertEquals("/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("//"));
    assertEquals("//", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/test/"));
    assertEquals("/test/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/test/11/"));
    assertEquals("/test/11/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/test//11"));
    assertEquals("/test//11", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#"));
    assertEquals("#", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/x"));
    assertEquals("#/x", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/abc/def/19"));
    assertEquals("#/abc/def/19", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/a~1c/d~0f"));
    assertEquals("#/a~1c/d~0f", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/", true));
    assertEquals("#/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#//", true));
    assertEquals("#//", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/test/", true));
    assertEquals("#/test/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/test/11/", true));
    assertEquals("#/test/11/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("#/test//11", true));
    assertEquals("#/test//11", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/", true));
    assertEquals("/", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex = assertThrows(InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/x", true));
    assertEquals("/x", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/abc/def/19", true));
    assertEquals("/abc/def/19", ex.getPointerString());
    log.info(ex, ex::getMessage);

    ex =
        assertThrows(
            InvalidJSONPointerSyntaxException.class, () -> new JSONPointer("/a~1c/d~0f", true));
    assertEquals("/a~1c/d~0f", ex.getPointerString());
    log.info(ex, ex::getMessage);
  }
}
