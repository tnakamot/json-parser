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

package com.github.tnakamot.json.parser;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.token.StringLocation;

/**
 * Thrown when a JSON lexical analyzer fails to tokenize a given JSON text.
 *
 * <p>
 * Instances of this class are immutable.
 *
 * <p>
 * TODO: document how the error messages are printed
 *
 */
public class JSONParserException extends Exception {
    private final String msg;
    private final JSONText source;
    private final StringLocation location;
    private final JSONParserErrorMessageFormat errMsgFmt;

    /**
     * Instantiate this exception.
     *
     * @param source       JSON text that has a problem
     * @param location     location of the problem within the source JSON text
     * @param errMsgFmt configuration to change the error message format of this exception
     * @param msg          error message which explains the problem
     */
    public JSONParserException(JSONText source,
                               StringLocation location,
                               JSONParserErrorMessageFormat errMsgFmt,
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
            String[] lines = source.get().split("\r|(\r?\n)");
            String line = lines[location.line() - 1];
            sb.append(System.lineSeparator());
            sb.append(line);
            sb.append(System.lineSeparator());
            for (int i = 1; i < location.column(); i++)
                sb.append(" ");
            sb.append("^");
        }

        return sb.toString();
    }
}
