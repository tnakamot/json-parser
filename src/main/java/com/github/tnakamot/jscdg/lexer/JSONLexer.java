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

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;

/**
 * An implementation of lexical analyzer for JSON texts.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONLexer {
    private final JSONText source;
    private final JSONLexerErrorMessageFormat errMsgConfig;
    private final PushbackReader reader;

    StringLocation location;

    protected JSONLexer(JSONText source, JSONLexerErrorMessageFormat errMsgConfig) {
        if (source == null) {
            throw new NullPointerException("source cannot be null");
        } else if (errMsgConfig == null) {
            throw new NullPointerException("errMsgConfig cannot be null");
        }

        this.source       = source;
        this.errMsgConfig = errMsgConfig;
        this.reader       = new PushbackReader(new StringReader(source.get()));
        this.location     = StringLocation.begin();
    }

    private int read() throws IOException {
        int ich = reader.read();
        if (ich == -1) {
            return -1;
        } else if (ich == '\r') {
            int ich2 = reader.read();
            if (ich2 == -1) {
                return -1;
            } else if (ich2 == '\n') {
                location = location.next(false).next(true);
                return ich2;
            } else {
                location = location.next(true);
                reader.unread(ich2);
                return ich;
            }
        } else if (ich == '\n') {
            location = location.next(true);
            return ich;
        } else {
            location = location.next(false);
            return ich;
        }
    }

    private char readChar() throws IOException, JSONLexerException {
        int ich = read();
        if (ich == -1)
            throw new JSONLexerException(source, location, errMsgConfig,
                    "reached EOF unexpectedly");

        return (char)ich;
    }

    private void pushBack(int ch) throws IOException {
        if (ch == '\n' || ch == '\r') {
            throw new UnsupportedOperationException("cannot push back \\n or \\r");
        }

        location = location.previous();
        reader.unread(ch);
    }

    /**
     * Read and return the next JSON token.
     *
     * @return the next token, or null if reached EOF
     * @throws IOException if I/O error happens
     * @throws JSONLexerException if there is a syntax error in JSON text
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
     */
    public JSONToken next() throws IOException, JSONLexerException {
        if (skipWhiteSpaces()) {
            return null;
        }

        StringLocation startLocation = location;
        char ch = readChar();

        switch (ch) {
            case '[':
                return new JSONToken(JSONTokenType.BEGIN_ARRAY, "[", startLocation, source);
            case ']':
                return new JSONToken(JSONTokenType.END_ARRAY, "]", startLocation, source);
            case '{':
                return new JSONToken(JSONTokenType.BEGIN_OBJECT, "{", startLocation, source);
            case '}':
                return new JSONToken(JSONTokenType.END_OBJECT, "}", startLocation, source);
            case ':':
                return new JSONToken(JSONTokenType.NAME_SEPARATOR, ":", startLocation, source);
            case ',':
                return new JSONToken(JSONTokenType.VALUE_SEPARATOR, ",", startLocation, source);
            case 't':
                pushBack(ch);
                expect(JSONTokenBoolean.JSON_TRUE);
                return new JSONTokenBoolean(JSONTokenBoolean.JSON_TRUE, startLocation, source);
            case 'f':
                pushBack(ch);
                expect(JSONTokenBoolean.JSON_FALSE);
                return new JSONTokenBoolean(JSONTokenBoolean.JSON_FALSE, startLocation, source);
            case 'n':
                pushBack(ch);
                expect(JSONTokenNull.JSON_NULL);
                return new JSONTokenNull(startLocation, source);
            case '"':
                pushBack(ch);
                return readString();
            case '-':
            case '0': case '1': case '2': case '3': case '4':
            case '5': case '6': case '7': case '8': case '9':
                pushBack(ch);
                return readNumber();
            default:
                throw new JSONLexerException(source, startLocation, errMsgConfig,
                        "unknown token starting with '" + ch +"'");
        }
    }

    private void expect(String expected)
            throws IOException, JSONLexerException
    {
        int expectedLen = expected.length();
        StringLocation originalLocation = location;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < expectedLen; i++ ) {
            char expectedCh = expected.charAt(i);
            char ch = readChar();
            sb.append(ch);

            if (ch != expectedCh) {
                throw new JSONLexerException(source, originalLocation, errMsgConfig,
                        "unknown token starting with '" + sb.toString() + "'");
            }
        }
    }

    /**
     * Read one JSON string token.
     *
     * @return an instance of {@link JSONTokenString} as a result of tokenization.
     * @throws IOException if I/O error happens
     * @throws JSONLexerException if there is a syntax error in JSON text
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-7">RFC 8259 - 7. Strings</a>
     */
    private JSONTokenString readString()
            throws IOException, JSONLexerException {
        StringLocation originalLocation = location;
        StringBuilder tokenText = new StringBuilder();
        StringBuilder strValue  = new StringBuilder();
        boolean escaped = false;

        char ch = readChar();
        if (ch != '"')
            throw new JSONLexerException(source, originalLocation, errMsgConfig,
                    "string token must start with '\"'");
        tokenText.append(ch);

        while (true) {
            ch = readChar();
            tokenText.append(ch);

            if (ch < 0x20) {
                StringBuilder errmsg = new StringBuilder();
                errmsg.append("control character ");
                errmsg.append(String.format("U+%04x", (int)ch));
                errmsg.append(" is not allowed in a JSON string token");
                throw new JSONLexerException(source, location.previous(), errMsgConfig,
                        errmsg.toString());
            }

            if (escaped) {
                switch (ch) {
                    case '"':
                    case '\\':
                    case '/':
                        strValue.append(ch);
                        break;
                    case 'b':
                        strValue.append('\b');
                        break;
                    case 'f':
                        strValue.append('\f');
                        break;
                    case 'n':
                        strValue.append('\n');
                        break;
                    case 'r':
                        strValue.append('\r');
                        break;
                    case 't':
                        strValue.append('\t');
                        break;
                    case 'u':
                        int unicode = 0;
                        for (int i = 0; i < 4; i++) {
                            char v = readChar();
                            tokenText.append(v);

                            unicode = unicode * 16;
                            if ('0' <= v && v <= '9') {
                                unicode = unicode + (v - '0');
                            } else if ('a' <= v && v <= 'f') {
                                unicode = unicode + (v - 'a') + 10;
                            } else if ('A' <= v && v <= 'F') {
                                unicode = unicode + (v - 'A') + 10;
                            } else {
                                throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                        "an Unicode escape sequence must consist of four characters of [0-9A-Fa-f]");
                            }
                        }

                        strValue.append((char) unicode);
                        break;
                    default:
                        throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                "unexpected character for an escape sequence");
                }

                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == '"') {
                break;
            } else {
                strValue.append(ch);
            }
        }

        return new JSONTokenString(tokenText.toString(), strValue.toString(), originalLocation, source);
    }

    private enum JSONNumberParserStage { MINUS, INT, FRAC, EXP }

    /**
     * Read one JSON number token.
     *
     * @return an instance of {@link JSONTokenNumber} as a result of tokenization.
     * @throws IOException if I/O error happens
     * @throws JSONLexerException if there is a syntax error in JSON text
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>
     */
    private JSONTokenNumber readNumber()
            throws IOException, JSONLexerException {
        StringLocation originalLocation = location;
        StringBuilder tokenText = new StringBuilder();
        JSONNumberParserStage stage = JSONNumberParserStage.MINUS;

        boolean negative = false;
        boolean intStartsWithZero = false;
        int numberOfDigitsInFrac = 0;
        int numberOfDigitsInExp  = 0;
        boolean expStartsWithSign = false;
        boolean moreToRead = true;
        char ch = '\0';

        while (moreToRead) {
            ch = readChar();
            tokenText.append(ch);

            switch (stage) {
                case MINUS:
                    if (ch == '-') {
                        // read the minus sign if it exists.
                        ch = readChar();
                        tokenText.append(ch);
                        negative = true;
                    }

                    if (ch == '0') {
                        // If the integer part of the number starts with zero, no leading
                        // zeros are not allowed by RFC 8259. Skip parsing the integer part.
                        stage = JSONNumberParserStage.INT;
                        intStartsWithZero = true;
                    } else if ('1' <= ch && ch <= '9') {
                        stage = JSONNumberParserStage.INT;
                        intStartsWithZero = false;
                    } else {
                        if (negative) {
                            throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                    "there must be a digit (0-9) right after the negative sign '-'");
                        } else {
                            throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                    "a number token must starts with a negative sign '-' or a digit (0-9)");
                        }
                    }
                    break;
                case INT:
                    if ((!intStartsWithZero) && '0' <= ch && ch <= '9') {
                        // Do nothing. Continue reading the integer part.
                    } else if (ch == '.') {
                        stage = JSONNumberParserStage.FRAC;
                    } else if (ch == 'e' || ch == 'E') {
                        stage = JSONNumberParserStage.EXP;
                    } else {
                        moreToRead = false; // exit the loop
                    }
                    break;
                case FRAC:
                    if ('0' <= ch && ch <= '9') {
                        numberOfDigitsInFrac += 1;
                    } else if (ch == 'e' || ch == 'E') {
                        stage = JSONNumberParserStage.EXP;
                    } else if (numberOfDigitsInFrac == 0){
                        throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                "there must be a digit (0-9) right after decimal point '.'");
                    } else {
                        moreToRead = false; // exit the loop
                    }
                    break;
                case EXP:
                    if (ch == '+' || ch == '-') {
                        expStartsWithSign = true;
                    } else if ('0' <= ch && ch <= '9') {
                        numberOfDigitsInExp += 1;
                    } else if (numberOfDigitsInExp == 0) {
                        if (expStartsWithSign) {
                            throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                    "there must be a digit (0-9) right after a sign ('+' or '-')");
                        } else {
                            throw new JSONLexerException(source, location.previous(), errMsgConfig,
                                    "there must be a digit (0-9) or a sign ('+' or '-') right after an exponent mark ('e' or 'E')");
                        }
                    } else {
                        moreToRead = false; // exit the loop
                    }
                    break;
                default:
                    throw new RuntimeException("the code must not reach here");
            }
        }

        // Push back the last character that was not considered as a part of this number token.
        pushBack(ch);
        tokenText.deleteCharAt(tokenText.length() - 1);

        return new JSONTokenNumber(tokenText.toString(), originalLocation, source);
    }

    /**
     * Skip insignificant white spaces. The white space characters are defined
     * in RFC 8259.
     *
     * @throws IOException if an I/O error occurs
     * @return true if the end of the text has been reached
     *
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
     */
    private boolean skipWhiteSpaces() throws IOException {
        while (true) {
            int ch = read();

            switch (ch) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    continue;
                case -1:
                    return true;
                default:
                    pushBack(ch);
                    return false;
            }
        }
    }
}
