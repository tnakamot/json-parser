package com.github.tnakamot.json.schema;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

public class JSONSchemaVersionTest {
  private static final Logger log = LoggerFactory.getLogger(JSONSchemaVersionTest.class);

  @Test
  public void testInvalidURIs() {
    InvalidJSONSchemaURIException ex;
    ex =
        assertThrows(
            InvalidJSONSchemaURIException.class,
            () -> JSONSchemaVersion.fromURI(new URI("http://json-schema.org/draft-08/schema#")));
    log.info(ex::getMessage);
  }

  @Test
  public void test201909() throws InvalidJSONSchemaURIException, URISyntaxException {
    JSONSchemaVersion version =
        JSONSchemaVersion.fromURI(new URI("http://json-schema.org/draft/2019-09/schema#"));
    assertEquals("2019-09", version.versionID());
    assertEquals("Draft 8", version.commonName());

    JSONSchemaVersion version2 =
        JSONSchemaVersion.fromURI(new URI("https://json-schema.org/draft/2019-09/schema#"));
    assertEquals("2019-09", version2.versionID());
    assertEquals("Draft 8", version2.commonName());
  }
}
