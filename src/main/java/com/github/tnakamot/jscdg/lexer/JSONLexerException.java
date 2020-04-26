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
 * Thrown when a JSON lexical analyzer fails to tokenize the given JSON text.
 */
public class JSONLexerException extends Exception {
    private final String msg;
    private final JSONText source;
    private final StringLocation location;
    private final JSONLexerErrorMessageConfiguration errMsgConfig;

    public JSONLexerException(JSONText source,
                              StringLocation location,
                              JSONLexerErrorMessageConfiguration errMsgConfig,
                              String msg)
    {
        super(msg);
        this.msg          = msg;
        this.source       = source;
        this.location     = location;
        this.errMsgConfig = errMsgConfig;
    }

    public JSONText source() {
        return source;
    }

    public StringLocation location() {
        return location;
    }

    @Override
    public String getMessage() {
        // TODO: write test cases

        StringBuilder sb = new StringBuilder();
        if (errMsgConfig.showFullPath()) {
            sb.append(source.fullName());
        } else {
            sb.append(source.name());
        }

        sb.append(":");

        if (errMsgConfig.showLineAndColumnNumber()) {
            sb.append(location.line());
            sb.append(":");
            sb.append(location.column());
        } else {
            sb.append(location.position());
        }

        sb.append(": ");
        sb.append(msg);

        if (errMsgConfig.showErrorLine()) {
            // TODO: show the error line
        }

        return sb.toString();
    }
}
