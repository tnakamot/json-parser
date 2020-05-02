package com.github.tnakamot.json;

import static org.junit.Assert.*;

import com.github.tnakamot.json.JSONText;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class JSONTextTest {
    private static final String JSON_STR = " { \"key\": \"My name is \u5d07\u5fd7\"} ";

    private File jsonFile;

    @Before
    public void setUp() throws IOException {
        jsonFile = File.createTempFile("JSONTextTest_", ".json");
        jsonFile.deleteOnExit();
        FileUtils.write(jsonFile, JSON_STR, StandardCharsets.UTF_8);
    }

    @After
    public void tearDown() {
        jsonFile.delete();
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
        file.delete();

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
        file.delete();
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
    public void testFromStringNull() throws IOException {
        assertThrows(
                NullPointerException.class,
                () -> JSONText.fromString(null));
    }
}
