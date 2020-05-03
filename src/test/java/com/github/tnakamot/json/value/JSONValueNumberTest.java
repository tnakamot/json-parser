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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collection;

public class JSONValueNumberTest {
    @Test
    public void testNull() {
        assertThrows(NullPointerException.class, () -> {
           JSONValueNumber num = new JSONValueNumber((String) null);
        });
    }

    @ParameterizedTest(name = "testInvalidNumber: \"{0}\"")
    @ValueSource(strings = {
            "",
            "xyz",
            "0x1b",
            "+1",
            "--12",
            "00015",
            "15..12",
            ".512",
            "1524.",
            "152xy",
            "124,123",
            "00",
            "1.512.23",
            "124e",
            "591E",
            "151 512",
            "876e +2",
            "153.e38",
            "e13",
            "-.1243",
            "NaN",
            "Inf",
            "-Inf",
    })
    public void testInvalidNumber(String text) {
        assertThrows(NumberFormatException.class, () -> {
            JSONValueNumber num = new JSONValueNumber(text);
        });
    }
}
