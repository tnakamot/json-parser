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
 * Represents one 'number" type token in JSON text.
 *
 * An instance of this class is immutable.
 */
public class JSONTokenNumber extends JSONToken {
    protected JSONTokenNumber(String text, StringLocation location, JSONText source) {
        super(JSONTokenType.NUMBER, text, location, source);

        /* TODO: check if the given token follows RFC 8259 and raise an exception if not. */
    }

    /**
     * This method returns a Java double value that this token represents.
     *
     * <p>
     * Note that all valid values of JSON "number" primitives can be represented by a Java double
     * value. For example, JSON "number" primitive allows very large number like 1E400, but
     * Java double cannot represent such value. If this JSON "number" primitive value cannot be
     * converted to a Java double value, this method throws {@link NumberFormatException}.
     *
     * @return a Java double value that this token represents
     * @throws NumberFormatException if the token cannot be interpreted as a Java double value
     */
    public double toDouble() {
        return Double.parseDouble(text());
    }

    /**
     * Returns if this token can be converted to a Java double value.
     *
     * @return whether this token can be converted to a Java double value.
     */
    public boolean canBeDouble() {
        try {
            toDouble();
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * This method returns a Java long value that this token represents.
     *
     * <p>
     * Note that all valid values of JSON "number" primitives can be represented by a Java long
     * value. For example, JSON "number" primitive allows a fraction, but Java long value
     * does not accept a fraction. If this JSON "number" primitive value cannot be converted to
     * a Java long value, this method raises {@link NumberFormatException}.
     *
     * @return a Java long value that this token represents.
     * @throws NumberFormatException if the token cannot be interpreted as a Java long value
     */
    public long toLong() throws NumberFormatException {
        return Long.parseLong(text(), 10);
    }

    /**
     * Returns if this token can be converted to a Java long value.
     *
     * @return whether this token can be converted to a Java long value.
     */
    public boolean canBeLong() {
        try {
            toLong();
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
