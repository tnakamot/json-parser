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
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JSONParserErrorHandlingOptionsTest {
  private static final Logger log = LoggerFactory.getLogger(JSONParserTest.class);

  @Test
  public void testDefault() {
    JSONParserErrorHandlingOptions opt = JSONParserErrorHandlingOptions.builder().build();
    assertFalse(opt.showURI());
    assertTrue(opt.showLineAndColumnNumber());
    assertFalse(opt.showErrorLine());
    assertFalse(opt.failOnDuplicateKey());
    assertFalse(opt.failOnTooBigNumber());
    assertEquals(System.err, opt.warningStream());
  }

  @Test
  public void testBuilder() {
    JSONParserErrorHandlingOptions opt =
        JSONParserErrorHandlingOptions.builder()
            .showURI(true)
            .showLineAndColumnNumber(false)
            .showErrorLine(true)
            .failOnDuplicateKey(true)
            .failOnTooBigNumber(true)
            .warningStream(System.out)
            .build();

    assertTrue(opt.showURI());
    assertFalse(opt.showLineAndColumnNumber());
    assertTrue(opt.showErrorLine());
    assertTrue(opt.failOnDuplicateKey());
    assertTrue(opt.failOnTooBigNumber());
    assertEquals(System.out, opt.warningStream());
  }

  @ParameterizedTest
  @ValueSource(strings = {"true", "false"})
  public void testShowURI(boolean opt) throws IOException, URISyntaxException {
    HttpServer httpServer = null;

    try {
      InputStream in =
          this.getClass().getResourceAsStream("/com/github/tnakamot/json/parser/invalid1.json");
      byte[] data = in.readAllBytes();

      httpServer = HttpServer.create(new InetSocketAddress(8215), 0);
      httpServer.createContext(
          "/json/invalid1.json",
          ex -> {
            ex.sendResponseHeaders(HttpURLConnection.HTTP_OK, data.length);
            ex.getResponseBody().write(data);
            ex.close();
          });
      httpServer.start();

      JSONParserErrorHandlingOptions options =
          JSONParserErrorHandlingOptions.builder().showURI(opt).build();

      String url = "http://localhost:8215/json/invalid1.json";
      JSONText jsText = JSONText.fromURL(new URL(url));
      JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(options));
      log.info(ex::getMessage);
    } finally {
      if (httpServer != null) {
        httpServer.stop(0);
      }
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"true", "false"})
  public void testShowLineAndColumnNumber(boolean opt) {
    String jsonStr = "[\n" + "  123,\n" + "  ff,\n" + "]";

    JSONParserErrorHandlingOptions options =
        JSONParserErrorHandlingOptions.builder().showLineAndColumnNumber(opt).build();

    JSONText jsText = JSONText.fromString(jsonStr, "test.json");
    JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(options));
    log.info(ex::getMessage);
  }

  @ParameterizedTest
  @ValueSource(strings = {"true", "false"})
  public void testShowErrorLine(boolean opt) {
    String jsonStr = "{\n" + "  \"key1\" true,\n" + "  \"key2\": false\n" + "}";

    JSONParserErrorHandlingOptions options =
        JSONParserErrorHandlingOptions.builder().showErrorLine(opt).build();

    JSONText jsText = JSONText.fromString(jsonStr, "test.json");
    JSONParserException ex = assertThrows(JSONParserException.class, () -> jsText.parse(options));
    log.info(ex::getMessage);
  }
}
