package com.github.tnakamot.json.schema;

import java.net.URI;
import org.jetbrains.annotations.NotNull;

public class InvalidJSONSchemaURIException extends Exception {
  private final URI uri;

  public InvalidJSONSchemaURIException(
      @NotNull URI uri, @NotNull JSONSchemaVersion[] validVersions) {
    super(
        "Invalid JSON Schema URI: "
            + uri.toString()
            + "\n"
            + "Valid JSON Schema versions that this library can recognize are \n"
            + UnsupportedJSONSchemaVersionException.getPrintableVersions(validVersions));
    this.uri = uri;
  }

  public URI uri() {
    return uri;
  }
}
