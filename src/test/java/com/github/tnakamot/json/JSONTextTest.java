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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONTextTest {
    private static final String JSON_STR = " { \"key\": \"My name is \u5d07\u5fd7\"} ";

    private static File jsonFile;

    @BeforeAll
    public static void setUp() throws IOException {
        jsonFile = File.createTempFile("JSONTextTest_", ".json");
        jsonFile.deleteOnExit();
        FileUtils.write(jsonFile, JSON_STR, StandardCharsets.UTF_8);
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
        assertEquals(file.getPath(), jsText.fullName());
        assertEquals(file.getName(), jsText.name());
    }

    @Test
    public void testFromFileNotExist() throws IOException {
        File file = File.createTempFile("JSONTextTest_", ".json");
        assertTrue(file.delete());

        assertThrows(
                FileNotFoundException.class,
                () -> JSONText.fromFile(file));
    }

    @Test
    public void testFromFileNull() {
        assertThrows(
                NullPointerException.class,
                () -> JSONText.fromFile(null));
    }

    @Test
    public void testFromURL() throws IOException {
        URL url = jsonFile.toURI().toURL();
        JSONText jsText = JSONText.fromURL(url);

        assertEquals(JSON_STR, jsText.get());
        assertEquals(url, jsText.source());
        assertEquals(url.toString(), jsText.fullName());
        assertEquals(jsonFile.getName(), jsText.name());
    }

    @Test
    public void testFromURLNotExist() throws IOException {
        File file = File.createTempFile("JSONTextTest_", ".json");
        assertTrue(file.delete());
        URL url = file.toURI().toURL();

        assertThrows(
                FileNotFoundException.class,
                () -> JSONText.fromURL(url));
    }

    @Test
    public void testFromURLNull() {
        assertThrows(
                NullPointerException.class,
                () -> JSONText.fromURL(null));
    }

    @Test
    public void testFromString() {
        JSONText jsText = JSONText.fromString(JSON_STR);

        assertEquals(JSON_STR, jsText.get());
        assertEquals(JSON_STR, jsText.source());
        assertEquals("(inner-string)", jsText.fullName());
        assertEquals("(inner-string)", jsText.name());
    }

    @Test
    public void testFromStringNull() {
        assertThrows(
                NullPointerException.class,
                () -> JSONText.fromString(null));
    }
}
