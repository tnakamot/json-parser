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

/**
 * Represents a location in a {@link String}.
 *
 * <p>
 * This basically has three properties, position, line and column.
 * Position and column are counted based on Unicode code units, which
 * means that a surrogate pair uses two positions (columns).
 *
 * <p>
 * Position starts from zero, while line and column start from one.
 *
 * <p>
 * A line is considered to be terminated by any one of a line feed
 * ('\n'), a carriage return ('\r'), or a carriage return followed
 * immediately by a linefeed.
 *
 * <p>
 * Assume this text:
 *
 * <pre>
 *  abc\ndef\r\nghi\rjkl\n\rmn
 * </pre>
 *
 * <p>
 * Then, the position, line and column of each character are as
 * shown below.
 *
 * <ul>
 *  <li>'a': position = 0,  line = 1, column = 1</li>
 *  <li>'b': position = 1,  line = 1, column = 2</li>
 *  <li>'c': position = 2,  line = 1, column = 3</li>
 *  <li>\n : position = 3,  line = 1, column = 4</li>
 *  <li>'d': position = 4,  line = 2, column = 1</li>
 *  <li>'e': position = 5,  line = 2, column = 2</li>
 *  <li>'f': position = 6,  line = 2, column = 3</li>
 *  <li>\r : position = 7,  line = 2, column = 4</li>
 *  <li>\n : position = 8,  line = 2, column = 5</li>
 *  <li>'g': position = 9,  line = 3, column = 1</li>
 *  <li>'h': position = 10, line = 3, column = 2</li>
 *  <li>'i': position = 11, line = 3, column = 3</li>
 *  <li>\r : position = 12, line = 3, column = 4</li>
 *  <li>'j': position = 13, line = 4, column = 1</li>
 *  <li>'k': position = 14, line = 4, column = 2</li>
 *  <li>'l': position = 15, line = 4, column = 3</li>
 *  <li>\n : position = 16, line = 4, column = 4</li>
 *  <li>\r : position = 17, line = 5, column = 1</li>
 *  <li>'m': position = 18, line = 6, column = 1</li>
 *  <li>'n': position = 19, line = 6, column = 2</li>
 * </ul>
 *
 * <p>
 * Instances of this class are immutable.
 */
public class StringLocation {
    private final StringLocation previous;
    private final int position;
    private final int line;
    private final int column;

    private StringLocation(StringLocation previous, int position, int line, int column) {
        this.previous = previous;
        this.position = position;
        this.line     = line;
        this.column   = column;

        if (position < 0) {
            throw new IllegalArgumentException("position must be zero or  positive");
        }
        if (line < 1) {
            throw new IllegalArgumentException("line must be positive");
        }
        if (column < 1) {
            throw new IllegalArgumentException("column must be positive");
        }
    }

    /**
     * @return position in a certain {@link String} starting from zero
     */
    public int position() {
        return position;
    }

    /**
     * @return line number in a certain {@link String} starting from one.
     */
    public int line() {
        return line;
    }

    /**
     * @return column number in a certain {@link String} starting from one.
     */
    public int column() {
        return column;
    }

    /**
     * @return an instance of {@link StringLocation} which represents the
     *         beginning of a {@link String}
     */
    public static StringLocation begin() {
        return new StringLocation(null,0, 1, 1);
    }

    /**
     * @return the instance of {@link StringLocation} which represents
     *         the previous location of this instance.
     */
    public StringLocation previous() {
        return previous;
    }

    /**
     * @param newline true if the next character is a new line
     * @return an instance of {@link StringLocation} which represents
     *         the next location of this instance.
     */
    public StringLocation next(boolean newline) {
        if (newline) {
            return new StringLocation(this, position + 1, line + 1, 1);
        } else {
            return new StringLocation(this, position + 1, line, column + 1);
        }
    }
}
