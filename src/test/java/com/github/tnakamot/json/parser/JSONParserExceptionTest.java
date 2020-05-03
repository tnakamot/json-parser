package com.github.tnakamot.json.parser;

import com.github.tnakamot.json.JSONText;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class JSONParserExceptionTest {
    private static final String JSON_STR = " { \"key\": My name is JSON } ";
    private static File jsonFile;
    private static final Logger log = LoggerFactory.getLogger(JSONParserExceptionTest.class);

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
    public void testShortName() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showFullPath(false)
                .build();

        File file = new File(jsonFile.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> {
            source.parse(fmt);
        });

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(source, ex.source());
        assertTrue(ex.getMessage().startsWith(file.getName()));
    }

    @Test
    public void testFullName() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showFullPath(true)
                .build();

        File file = new File(jsonFile.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> {
            source.parse(fmt);
        });

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(source, ex.source());
        assertTrue(ex.getMessage().startsWith(file.getPath()));
    }

    @Test
    public void testPosition() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(false)
                .build();

        File file = new File(jsonFile.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> {
            source.parse(fmt);
        });

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(10, ex.location().position());

        String positionStr = ex.getMessage().split(":")[1];
        assertEquals("10", positionStr);
    }


    @Test
    public void testLineAndColumn() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(true)
                .build();

        File file = new File(jsonFile.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> {
            source.parse(fmt);
        });

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(1, ex.location().line());
        assertEquals(11, ex.location().column());

        String lineStr   = ex.getMessage().split(":")[1];
        String columnStr = ex.getMessage().split(":")[2];
        assertEquals("1", lineStr);
        assertEquals("11", columnStr);
    }
}
