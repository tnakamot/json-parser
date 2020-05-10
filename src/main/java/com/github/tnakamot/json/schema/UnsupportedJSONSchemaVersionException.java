package com.github.tnakamot.json.schema;

import java.net.URI;

public class UnsupportedJSONSchemaVersionException extends Exception {
  public UnsupportedJSONSchemaVersionException(
      JSONSchemaVersion version, JSONSchemaVersion[] supportedVersions) {
    super(
        "JSON Scheme "
            + version.commonName()
            + " is not supported.\n"
            + "Supported JSON Schema versions are \n"
            + getPrintableVersions(supportedVersions));
  }

  static String getPrintableVersions(JSONSchemaVersion[] versions) {
    StringBuilder s = new StringBuilder();
    for (JSONSchemaVersion version : versions) {
      s.append("[");
      s.append(version.commonName());
      s.append("] ");
      s.append("(ID: ");
      s.append(version.versionID());
      s.append(")\n");
      for (URI uri : version.URIs()) {
        s.append("  ");
        s.append(uri.toString());
        s.append("\n");
      }
    }
    return s.toString();
  }
}
