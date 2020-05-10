package com.github.tnakamot.json.schema;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.dmfs.rfc3986.Uri;
import org.dmfs.rfc3986.encoding.Precoded;
import org.dmfs.rfc3986.uris.LazyUri;
import org.dmfs.rfc3986.uris.Normalized;
import org.dmfs.rfc3986.uris.Text;

/** Represent a version of JSON Schema (e.g. 2019-09) */
public class JSONSchemaVersion {
  private static final String DRAFT_06_ID = "draft-06";
  private static final String DRAFT_07_ID = "draft-07";
  private static final String DRAFT_2019_09_ID = "2019-09";

  public static final JSONSchemaVersion DRAFT_06 =
      new JSONSchemaVersion(
          DRAFT_06_ID,
          "Draft 6",
          new Uri[] {
            new LazyUri(new Precoded("http://json-schema.org/draft-06/schema#")),
            new LazyUri(new Precoded("https://json-schema.org/draft-06/schema#")),
          });

  public static final JSONSchemaVersion DRAFT_07 =
      new JSONSchemaVersion(
          DRAFT_07_ID,
          "Draft 7",
          new Uri[] {
            new LazyUri(new Precoded("http://json-schema.org/draft-07/schema#")),
            new LazyUri(new Precoded("https://json-schema.org/draft-07/schema#")),
          });

  public static final JSONSchemaVersion DRAFT_2019_09 =
      new JSONSchemaVersion(
          DRAFT_2019_09_ID,
          "Draft 8",
          new Uri[] {
            new LazyUri(new Precoded("http://json-schema.org/draft/2019-09/schema#")),
            new LazyUri(new Precoded("https://json-schema.org/draft/2019-09/schema#")),
          });

  public static final JSONSchemaVersion[] VALID_VERSIONS = {
    DRAFT_06, DRAFT_07, DRAFT_2019_09,
  };

  public static final JSONSchemaVersion[] SUPPORTED_VERSIONS = {DRAFT_2019_09};

  public static final JSONSchemaVersion DEFAULT_VERSION = DRAFT_2019_09;

  private final String versionId;
  private final String commonName;
  private final Collection<Uri> uris;

  private JSONSchemaVersion(String versionId, String commonName, Uri[] uris) {
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

  public Uri[] URIs() {
    return uris.toArray(new Uri[0]);
  }

  public static JSONSchemaVersion fromURI(String uriStr) throws InvalidJSONSchemaURIException {
    String normalizedUriStr =
        new Text(new Normalized(new LazyUri(new Precoded(uriStr)))).toString();

    for (JSONSchemaVersion version : VALID_VERSIONS) {
      for (Uri verUri : version.URIs()) {
        String nVerUri = new Text(new Normalized(verUri)).toString();
        if (nVerUri.equals(normalizedUriStr)) {
          return version;
        }
      }
    }

    throw new InvalidJSONSchemaURIException(uriStr, VALID_VERSIONS);
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
