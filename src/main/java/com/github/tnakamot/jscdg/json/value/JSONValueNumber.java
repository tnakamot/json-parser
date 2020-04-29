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

package com.github.tnakamot.jscdg.json.value;

import com.github.tnakamot.jscdg.json.token.JSONTokenNumber;

/**
 * Represents one JSON 'number' value.
 *
 * <p>
 * According to
 * <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>,
 * JSON does not set limit for 'number' values. Very large or very precise numbers
 * cannot be represented by Java primitive types. Therefore, this class internally
 * holds a text representation of a JSON 'number' value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public class JSONValueNumber extends JSONValuePrimitive {
    private final String text;

    /**
     * Create an instance of a Java representation of a JSON number value.
     *
     * @param text value represented by a Java {@link String} object of this
     *              JSON string value. Cannot be null.
     */
    public JSONValueNumber(String text) {
        super(JSONValueType.NUMBER, null);
        this.text = text;

        if (text == null) {
            throw new NullPointerException("text cannot be null");
        }

        // TODO: validate the text
    }

    /**
     * Create an instance of a Java representation of a JSON number value
     * from a Java long value.
     *
     * @param value Java long value which represents this JSON number value
     */
    public JSONValueNumber(long value) {
        super(JSONValueType.NUMBER, null);
        this.text = Long.toString(value, 10);
    }

    /**
     * Create an instance of a Java representation of a JSON number value
     * from a Java double value.
     *
     * @param value Java long value which represents this JSON number value
     * @throws IllegalArgumentException if the given value is NaN, +Inf or -Inf
     */
    public JSONValueNumber(double value) throws IllegalArgumentException {
        super(JSONValueType.NUMBER, null);
        this.text = Double.toString(value);

        if (Double.isNaN(value)) {
            throw new IllegalArgumentException("JSON number value cannot be NaN");
        } else if (Double.isInfinite(value)) {
            throw new IllegalArgumentException("JSON number value cannot be infinite");
        }
    }

    /**
     * Create an instance of a Java representation of a JSON number value
     * from a JSON number token.
     *
     * @param token source token of this JSON string value.
     */
    public JSONValueNumber(JSONTokenNumber token) {
        super(JSONValueType.STRING, token);
        this.text = token.text();

        // Because JSONTokenNumber#text() returns a text representation
        // of a JSON number value that complies with RFC 8259, this method
        // does not validate the text to minimize the computational overhead.
    }

    /**
     * Return a JSON text representation of this JSON number value.
     *
     * <p>
     * The returned text is not normalized. For example, there are many
     * JSON texts that represent one thousand; 1000, 1.0e3, 1000.00.
     * This method returns one of valid JSON text representations.
     * There is no guarantee that this method returns the same text
     * representation for the same mathematical value.
     *
     * <p>
     * Returned text always complies with
     * <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>.
     *
     * @return text which represents this JSON number value.
     */
    public String text() {
        return text;
    }

    /**
     * This method returns a Java double value that this JSON number represents.
     *
     * <p>
     * Note that not all valid values of JSON "number" primitives can be precisely represented
     * by a Java double value. For example, JSON "number" primitive allows very large number
     * like 1E400, but Java double treats such large value as {@link Double#POSITIVE_INFINITY}.
     * Or, some fractions may be simply ignored. Therefore, the application programmer who calls
     * this method must not assume that the returned double value precisely represents the
     * original number in the JSON text.
     *
     * <p>
     * TODO: write unit tests, clarify mapping between the text and Java double representation
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
     * <p>
     * TODO: write unit tests
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

    @Override
    public String toString() {
        return text;
    }
}
