package com.github.tnakamot.json.schema;

public class InvalidJSONSchemaURIException extends Exception {
  public InvalidJSONSchemaURIException(String uriStr, JSONSchemaVersion[] validVersions) {
    super(
        "Invalid JSON Schema URI: "
            + uriStr
            + "\n"
            + "Valid JSON Schema versions that this library can recognize are \n"
            + UnsupportedJSONSchemaVersionException.getPrintableVersions(validVersions));
  }
}
