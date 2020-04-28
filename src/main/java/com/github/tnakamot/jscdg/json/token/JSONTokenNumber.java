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

package com.github.tnakamot.jscdg.json.token;

import com.github.tnakamot.jscdg.json.JSONText;
import com.github.tnakamot.jscdg.json.value.JSONValueNumber;

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

    /**
     * Creates one "number" type token of a JSON text.
     *
     * <p>
     * It is the caller's responsibility to validate the token text as number
     * before creating this instance.
     *
     * @param text     text of this token
     * @param location location of this token within the source JSON text
     * @param source   source JSON text where this token was extracted from
     */
    public JSONTokenNumber(String text, StringLocation location, JSONText source) {
        super(JSONTokenType.NUMBER, text, location, source);

        // TODO: validate the text
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
     * @deprecated
     * The equivalent method has been implemented as {@link JSONValueNumber#toDouble()}.
     * To remove duplicate of the same methods, this method will be removed.
     *
     * @return a Java double value that this token represents
     * @throws NumberFormatException if the token cannot be interpreted as a Java double value
     */
    @Deprecated
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
     * @deprecated
     * The equivalent method has been implemented as {@link JSONValueNumber#toLong()}.
     * To remove duplicate of the same methods, this method will be removed.
     *
     * @return a Java long value that this token represents.
     * @throws NumberFormatException if the token cannot be interpreted as a Java long value
     */
    @Deprecated
    public long toLong() throws NumberFormatException {
        return Long.parseLong(text(), 10);
    }

    /**
     * Returns if this token can be converted to a Java long value.
     * If this method returns true, {@link #toLong()} can be executed
     * without an exception.
     *
     * @deprecated
     * The equivalent method has been implemented as {@link JSONValueNumber#canBeLong()}.
     * To remove duplicate of the same methods, this method will be removed.
     *
     * @return whether this token can be converted to a Java long value.
     */
    @Deprecated
    public boolean canBeLong() {
        try {
            toLong();
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    /**
     * Text representation of this token as it appears in the source JSON text.
     *
     * <p>
     * The returned text always complies with
     * <a href="https://tools.ietf.org/html/rfc8259#section-6">RFC 8259 - 6. Numbers</a>.
     *
     * @return text representation of this token as it appears in the source JSON text.
     */
    public String text() {
        return text;
    }
}
