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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONParserErrorHandlingOptionsTest {
  @Test
  public void testDefault() {
    JSONParserErrorHandlingOptions fmt = JSONParserErrorHandlingOptions.builder().build();
    assertFalse(fmt.showURI());
    assertTrue(fmt.showLineAndColumnNumber());
    assertFalse(fmt.showErrorLine());
  }

  @Test
  public void testBuilder() {
    JSONParserErrorHandlingOptions fmt =
        JSONParserErrorHandlingOptions.builder()
            .showURI(true)
            .showLineAndColumnNumber(false)
            .showErrorLine(true)
            .build();

    assertTrue(fmt.showURI());
    assertFalse(fmt.showLineAndColumnNumber());
    assertTrue(fmt.showErrorLine());
  }
}
