package com.github.tnakamot.json.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONParserErrorMessageFormatTest {
    @Test
    public void testDefault() {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder().build();
        assertFalse(fmt.showFullPath());
        assertTrue(fmt.showLineAndColumnNumber());
        assertFalse(fmt.showErrorLine());
    }

    @Test
    public void testBuilder() {
        JSONParserErrorMessageFormat fmt
                = JSONParserErrorMessageFormat.builder()
                .showFullPath(true)
                .showLineAndColumnNumber(false)
                .showErrorLine(true)
                .build();

        assertTrue(fmt.showFullPath());
        assertFalse(fmt.showLineAndColumnNumber());
        assertTrue(fmt.showErrorLine());
    }
}
