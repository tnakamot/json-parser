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

package com.github.tnakamot.json.value;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JSONValueNullTest {
    @Test
    public void testEqulity() throws IOException, JSONParserException {
        JSONValueNull val1 = JSONValueNull.INSTANCE;
        JSONValueNull val2 = (JSONValueNull) JSONText.fromString("null").parse();
        JSONValueNull val3 = (JSONValueNull) JSONText.fromString(" null ").parse();

        assertEquals(val1.hashCode(), val2.hashCode());
        assertEquals(val1.hashCode(), val3.hashCode());

        assertEquals(val1, val2);
        assertEquals(val2, val1);
        assertEquals(val1, val3);
        assertEquals(val3, val1);
        assertEquals(val2, val3);
        assertEquals(val3, val2);

        assertEquals("null", val1.toString());
        assertEquals("null", val2.toString());
        assertEquals("null", val3.toString());
    }

    @Test
    public void testToString() throws IOException, JSONParserException {
        JSONValueNull val1 = JSONValueNull.INSTANCE;
        JSONValueNull val2 = (JSONValueNull) JSONText.fromString("null").parse();

        assertEquals("null", val1.toString());
        assertEquals("null", val2.toString());
    }
}
