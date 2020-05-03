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
    private static final String JSON_STR_SINGLE_LINE = " { \"key\": My name is JSON } ";
    private static final String JSON_STR_MULTI_LINES = "{\n  \"key\": My name is JSON\n}";

    private static File jsonFileSingleLine;
    private static File jsonFileMultiLines;

    private static final Logger log = LoggerFactory.getLogger(JSONParserExceptionTest.class);

    @BeforeAll
    public static void setUp() throws IOException {
        jsonFileSingleLine = File.createTempFile("JSONTextTest_", ".json");
        jsonFileSingleLine.deleteOnExit();
        FileUtils.write(jsonFileSingleLine, JSON_STR_SINGLE_LINE, StandardCharsets.UTF_8);

        jsonFileMultiLines = File.createTempFile("JSONTextTest_", ".json");
        jsonFileMultiLines.deleteOnExit();
        FileUtils.write(jsonFileMultiLines, JSON_STR_MULTI_LINES, StandardCharsets.UTF_8);
    }

    @AfterAll
    public static void tearDown() {
        assertTrue(jsonFileSingleLine.delete());
        assertTrue(jsonFileMultiLines.delete());
    }

    @Test
    public void testShortName() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showFullPath(false)
                .build();

        File file = new File(jsonFileSingleLine.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

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

        File file = new File(jsonFileSingleLine.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(source, ex.source());
        assertTrue(ex.getMessage().startsWith(file.getPath()));
    }

    @Test
    public void testPositionSingleLine() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(false)
                .build();

        File file = new File(jsonFileSingleLine.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(10, ex.location().position());

        String positionStr = ex.getMessage().split(":")[1];
        assertEquals("10", positionStr);
    }

    @Test
    public void testLineAndColumnSingleLine() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(true)
                .build();

        File file = new File(jsonFileSingleLine.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(1, ex.location().line());
        assertEquals(11, ex.location().column());

        String lineStr   = ex.getMessage().split(":")[1];
        String columnStr = ex.getMessage().split(":")[2];
        assertEquals("1", lineStr);
        assertEquals("11", columnStr);
    }

    @Test
    public void testPositionMultiLines() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(false)
                .build();

        File file = new File(jsonFileMultiLines.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(11, ex.location().position());

        String positionStr = ex.getMessage().split(":")[1];
        assertEquals("11", positionStr);
    }

    @Test
    public void testLineAndColumnMultiLines() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showLineAndColumnNumber(true)
                .build();

        File file = new File(jsonFileMultiLines.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(2, ex.location().line());
        assertEquals(10, ex.location().column());

        String lineStr   = ex.getMessage().split(":")[1];
        String columnStr = ex.getMessage().split(":")[2];
        assertEquals("2", lineStr);
        assertEquals("10", columnStr);
    }

    @Test
    public void testErrorLineSingleLine() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showErrorLine(true)
                .build();

        File file = new File(jsonFileSingleLine.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(1, ex.location().line());
        assertEquals(11, ex.location().column());

        String errorLine = ex.getMessage().split(System.lineSeparator())[1];
        assertEquals(JSON_STR_SINGLE_LINE, errorLine);
    }

    @Test
    public void testErrorLineMultiLines() throws IOException {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showErrorLine(true)
                .build();

        File file = new File(jsonFileMultiLines.getPath());
        JSONText source = JSONText.fromFile(file);
        JSONParserException ex = assertThrows(JSONParserException.class, () -> source.parse(fmt));

        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        log.info(() -> methodName + ": " + ex.getMessage());

        assertEquals(2, ex.location().line());
        assertEquals(10, ex.location().column());

        String errorLine = ex.getMessage().split(System.lineSeparator())[1];
        String expectedLine = JSON_STR_MULTI_LINES.split("\n")[1];
        assertEquals(expectedLine, errorLine);
    }
}
