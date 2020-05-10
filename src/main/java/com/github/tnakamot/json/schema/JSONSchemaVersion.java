package com.github.tnakamot.json.schema;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;

/** Represent a version of JSON Schema (e.g. 2019-09) */
public class JSONSchemaVersion {
  private static final String DRAFT_06_ID = "draft-06";
  private static final String DRAFT_07_ID = "draft-07";
  private static final String DRAFT_2019_09_ID = "2019-09";

  /**
   * Utility function to convert a string to URI. This method wraps {@link URISyntaxException} with
   * {@link RuntimeException}, so that the conversion from a String to URI in static initializers do
   * not have explicitly handle the exception. This method should be used only if the programmer
   * knows the given string conforms to the URI syntax.
   *
   * @param uriStr a string which represents an URI
   * @return an instance of URI
   */
  private static URI uri(String uriStr) {
    try {
      return new URI(uriStr);
    } catch (URISyntaxException ex) {
      throw new RuntimeException("the code must not reach here", ex);
    }
  }

  public static final JSONSchemaVersion DRAFT_06 =
      new JSONSchemaVersion(
          DRAFT_06_ID,
          "Draft 6",
          new URI[] {
            uri("http://json-schema.org/draft-06/schema#"),
            uri("https://json-schema.org/draft-06/schema#"),
          });

  public static final JSONSchemaVersion DRAFT_07 =
      new JSONSchemaVersion(
          DRAFT_07_ID,
          "Draft 7",
          new URI[] {
            uri("http://json-schema.org/draft-07/schema#"),
            uri("https://json-schema.org/draft-07/schema#"),
          });

  public static final JSONSchemaVersion DRAFT_2019_09 =
      new JSONSchemaVersion(
          DRAFT_2019_09_ID,
          "Draft 8",
          new URI[] {
            uri("http://json-schema.org/draft/2019-09/schema#"),
            uri("https://json-schema.org/draft/2019-09/schema#"),
          });

  public static final JSONSchemaVersion[] VALID_VERSIONS = {
    DRAFT_06, DRAFT_07, DRAFT_2019_09,
  };

  public static final JSONSchemaVersion[] SUPPORTED_VERSIONS = {DRAFT_2019_09};
  public static final JSONSchemaVersion DEFAULT_VERSION = DRAFT_2019_09;

  private final String versionId;
  private final String commonName;
  private final Collection<URI> uris;

  private JSONSchemaVersion(String versionId, String commonName, URI[] uris) {
    this.versionId = versionId;
    this.commonName = commonName;
    this.uris = Collections.unmodifiableCollection(Arrays.asList(uris));
  }

  public String versionID() {
    return versionId;
  }

  public String commonName() {
    return commonName;
  }

  public URI[] URIs() {
    return uris.toArray(new URI[0]);
  }

  public static JSONSchemaVersion fromURI(@NotNull URI uri) throws InvalidJSONSchemaURIException {
    URI norm = uri.normalize();

    for (JSONSchemaVersion version : VALID_VERSIONS) {
      for (URI verUri : version.URIs()) {
        if (verUri.equals(norm)) {
          return version;
        }
      }
    }

    throw new InvalidJSONSchemaURIException(uri, VALID_VERSIONS);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj instanceof JSONSchemaVersion) {
      JSONSchemaVersion version = (JSONSchemaVersion) obj;
      return version.versionId.equals(this.versionId);
    } else {
      return false;
    }
  }
}
