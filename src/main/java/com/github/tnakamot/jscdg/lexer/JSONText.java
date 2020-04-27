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

package com.github.tnakamot.jscdg.lexer;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one JSON text.
 *
 * <p>
 * Instances of this class are immutable.
 *
 * <p>
 * TODO: support {@link InputStream} as an input.
 */
public class JSONText {
    private final String text;
    private final Object source;

    private JSONText(String text, Object source) {
        this.text       = text;
        this.source     = source;

        if ( ! ((source instanceof File) ||
                (source instanceof URL)  ||
                (source instanceof String))) {
            throw new IllegalArgumentException("source must be File, URL or String");
        }
    }

    /**
     * @return Contents of this JSON text.
     */
    public String get() {
        return text;
    }

    /**
     * Return the object which represents the source of this JSON text.
     *
     * @return An instance of {@link File}, {@link URL} or {@link String}.
     */
    public Object source() {
        return source;
    }

    /**
     * Tokenize the JSON text.
     *
     * @return Sequence of JSON tokens.
     * @throws IOException if an I/O error occurs
     * @throws JSONLexerException if there is a syntax error in JSON text
     */
    public List<JSONToken> tokens(JSONLexerErrorMessageFormat errMsgFmt)
            throws IOException, JSONLexerException {
        List<JSONToken> tokens = new ArrayList<>();
        JSONLexer lexer = new JSONLexer(this, errMsgFmt);
        JSONToken token;

        while ((token = lexer.next()) != null)
            tokens.add(token);

        return tokens;
    }

    /**
     * Tokenize the JSON text.
     *
     * @return Sequence of JSON tokens.
     * @throws IOException if an I/O error occurs
     * @throws JSONLexerException if there is a syntax error in JSON text
     */
    public List<JSONToken> tokens()
            throws IOException, JSONLexerException {
        JSONLexerErrorMessageFormat errMsgFmt =
                JSONLexerErrorMessageFormat.builder().build();
        return tokens(errMsgFmt);
    }

    /**
     * Return a string which represents the name of the source of this JSON text.
     * This is useful to construct an error message of tokenizers and parsers.
     *
     * @return
     */
    public String name() {
        if (source instanceof File) {
            return ((File) source).getName();
        } else if (source instanceof URL) {
            URL url = (URL) source;
            String[] paths = url.getPath().split("/");
            if (paths.length == 0) {
                return url.toString();
            } else {
                return paths[paths.length - 1];
            }
        } else if (source instanceof String) {
            return "(inner-string)";
        }

        throw new UnsupportedOperationException("source must be File, URL or String");
    }

    /**
     * Return a string which represents the full path of the source of this JSON text.
     * This is useful to construct an error message of tokenizers and parsers.
     *
     * @return
     */
    public String fullName() {
        if (source instanceof File) {
            return ((File) source).toString();
        } else if (source instanceof URL) {
            return ((URL) source).toString();
        } else if (source instanceof String) {
            return "(inner-string)";
        }

        throw new UnsupportedOperationException("source must be File, URL or String");
    }

    @Override
    public String toString() {
        return "[JSONText from " + name() + "]";
    }

    /**
     * Reads JSON text from a file and returns an instance of {@link JSONText} for further
     * processing. The file must be encoded using UTF-8.
     *
     * @param file JSON text file.
     * @return An instance of JSON text.
     * @throws IOException
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character Encoding</a>
     */
    public static JSONText fromFile(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file cannot be null");
        }

        String text = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        return new JSONText(text, file);
    }

    /**
     * Reads JSON text from a URL and returns an instance of {@link JSONText} for further
     * processing. The text must be encoded using UTF-8.
     *
     * @param url URL of a JSON text file.
     * @return An instance of JSON text.
     * @throws IOException
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character Encoding</a>
     */
    public static JSONText fromURL(URL url) throws IOException {
        if (url == null) {
            throw new NullPointerException("url cannot be null");
        }

        /*
         * TODO: RFC 8259 allows to add BOM (U+FEFF) to the beginning of
         *       a networked-transmitted JSON text. To improve the interoperability
         *       remove the BOM if exists.
         */
        String text = IOUtils.toString(url, StandardCharsets.UTF_8);
        return new JSONText(text, url);
    }

    /**
     * Convert the given String to an instance of {@link JSONText}.
     *
     * @param str A string which contains JSON text.
     * @return An instance of JSON text.
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1. Character Encoding</a>
     */
    public static JSONText fromString(String str) {
        if (str == null) {
            throw new NullPointerException("str cannot be null");
        }

        return new JSONText(str, str);
    }
}
