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

package com.github.tnakamot.json;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URISyntaxException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;

public class JSONTextTest {
  private static final String JSON_STR = " { \"key\": \"My name is \u5d07\u5fd7\"} ";

  private static File jsonFile;

  private static final Logger log = LoggerFactory.getLogger(JSONTextTest.class);

  @BeforeAll
  public static void setUp() throws IOException {
    jsonFile = File.createTempFile("JSONTextTest_", ".json");
    jsonFile.deleteOnExit();
    Files.writeString(jsonFile.toPath(), JSON_STR, StandardCharsets.UTF_8);
  }

  @AfterAll
  public static void tearDown() {
    assertTrue(jsonFile.delete());
  }

  @Test
  public void testFromFile() throws IOException {
    File file = new File(jsonFile.getPath());
    JSONText jsText = JSONText.fromFile(file);

    assertEquals(JSON_STR, jsText.get());
    assertEquals(file, jsText.source());
    assertEquals(file.toURI(), jsText.uri());
    assertEquals(file.getName(), jsText.name());

    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info(() -> methodName + ": (original): " + file.getPath());
    log.info(() -> methodName + ": uri()     : " + jsText.uri());
    log.info(() -> methodName + ": name()    : " + jsText.name());
  }

  @Test
  public void testFromFileNotExist() throws IOException {
    File file = File.createTempFile("JSONTextTest_", ".json");
    assertTrue(file.delete());

    assertThrows(NoSuchFileException.class, () -> JSONText.fromFile(file));
  }

  @Test
  public void testFromURL() throws IOException, URISyntaxException {
    URL url = jsonFile.toURI().toURL();
    JSONText jsText = JSONText.fromURL(url);

    assertEquals(JSON_STR, jsText.get());
    assertEquals(url, jsText.source());
    assertEquals(url.toURI(), jsText.uri());
    assertEquals(jsonFile.getName(), jsText.name());

    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info(() -> methodName + ": (original): " + url);
    log.info(() -> methodName + ": uri()     : " + jsText.uri());
    log.info(() -> methodName + ": name()    : " + jsText.name());
  }

  @Test
  public void testFromURLNotExist() throws IOException {
    File file = File.createTempFile("JSONTextTest_", ".json");
    assertTrue(file.delete());
    URL url = file.toURI().toURL();

    assertThrows(FileNotFoundException.class, () -> JSONText.fromURL(url));
  }

  @Test
  public void testFromString() {
    JSONText jsText = JSONText.fromString(JSON_STR);

    assertEquals(JSON_STR, jsText.get());
    assertEquals(JSON_STR, jsText.source());

    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info(() -> methodName + ": uri()     : " + jsText.uri());
    log.info(() -> methodName + ": name()    : " + jsText.name());
  }

  @Test
  public void testFromStringWithName() {
    JSONText jsText = JSONText.fromString(JSON_STR, "test.json");

    assertEquals(JSON_STR, jsText.get());
    assertEquals(JSON_STR, jsText.source());
    assertEquals("test.json", jsText.name());

    String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
    log.info(() -> methodName + ": uri()     : " + jsText.uri());
    log.info(() -> methodName + ": name()    : " + jsText.name());
  }
}
