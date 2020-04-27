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

/**
 * Thrown when a JSON lexical analyzer fails to tokenize a given JSON text.
 *
 * <p>
 * Instances of this class are immutable.
 *
 * <p>
 * TODO: document how the error messages are printed
 *
 * <p>
 * TODO: support {@link JSONLexerErrorMessageFormat#showErrorLine()}
 *
 */
public class JSONLexerException extends Exception {
    private final String msg;
    private final JSONText source;
    private final StringLocation location;
    private final JSONLexerErrorMessageFormat errMsgFmt;

    /**
     * Instantiate this exception.
     *
     * @param source       JSON text that has a problem
     * @param location     location of the problem within the source JSON text
     * @param errMsgFmt configuration to change the error message format of this exception
     * @param msg          error message which explains the problem
     */
    public JSONLexerException(JSONText source,
                              StringLocation location,
                              JSONLexerErrorMessageFormat errMsgFmt,
                              String msg)
    {
        super(msg);
        this.msg       = msg;
        this.source    = source;
        this.location  = location;
        this.errMsgFmt = errMsgFmt;
    }

    /**
     * @return the JSON text that cannot be tokenized properly.
     */
    public JSONText source() {
        return source;
    }

    /**
     * Returns the location of hte problem where the lexical analyzer failed to tokenize
     * the given JSON text.
     *
     * @return location of the problem within the source JSON text
     */
    public StringLocation location() {
        return location;
    }

    @Override
    public String getMessage() {
        // TODO: write test cases

        StringBuilder sb = new StringBuilder();
        if (errMsgFmt.showFullPath()) {
            sb.append(source.fullName());
        } else {
            sb.append(source.name());
        }

        sb.append(":");

        if (errMsgFmt.showLineAndColumnNumber()) {
            sb.append(location.line());
            sb.append(":");
            sb.append(location.column());
        } else {
            sb.append(location.position());
        }

        sb.append(": ");
        sb.append(msg);

        if (errMsgFmt.showErrorLine()) {
            // TODO: show the error line
        }

        return sb.toString();
    }
}
