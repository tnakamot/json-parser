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
 * Represents one "number" type token in JSON text.
 *
 * <p>
 * The representation of a number in JSON is defined by
 * <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>.
 * This RFC specification does not set limits of the range and precision of numbers.
 * Although the specification allows software implementations to set limits, this
 * implementation does not practically set limits. An application program can get
 * the original text that represents this number without loosing precision by calling
 * {@link #text()} to maximize the interoperability.
 *
 * <p>
 * However, in reality, most of the Java application programs internally use
 * {@link Double} or {@link Long} (or their corresponding primitives) for data
 * storage and calculation. This class provides methods to convert the "number"
 * in JSON text into those types (e.g. {@link #toDouble()}, {@link #toLong()}).
 *
 * <p>
 * Instances of this class are immutable.
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
     * Note that not all valid values of JSON "number" primitives can be precisely represented
     * by a Java double value. For example, JSON "number" primitive allows very large number
     * like 1E400, but Java double treats such large value as {@link Double#POSITIVE_INFINITY}.
     * Or, some fractions may be simply ignored. Therefore, the application programmer who calls
     * this method must not assume that the returned double value precisely represents the
     * original number in the JSON text.
     *
     * @return a Java double value that this token represents
     * @throws NumberFormatException if the token cannot be interpreted as a Java double value
     */
    public double toDouble() {
        return Double.parseDouble(text());
    }

    /**
     * This method returns a Java long value that this token represents.
     *
     * <p>
     * Note that not all valid values of JSON "number" primitives can be represented by a Java long
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
     * If this method returns true, {@link #toLong()} can be executed
     * without an exception.
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
