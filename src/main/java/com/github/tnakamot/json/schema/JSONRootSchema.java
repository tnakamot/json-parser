package com.github.tnakamot.json.schema;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import com.github.tnakamot.json.value.JSONValue;
import com.github.tnakamot.json.value.JSONValueObject;
import com.github.tnakamot.json.value.JSONValueType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class JSONRootSchema extends JSONSchema {
  private final JSONSchemaVersion schemaVersion;
  private final URI schemaURI;
  private final JSONValueObject root;

  private JSONRootSchema(JSONValueObject root) {
    this.root = root;

    if (root.containsKey("$schema")) {
      try {
        schemaURI = new URI(root.getString("$schema"));
        schemaVersion = JSONSchemaVersion.fromURI(this.schemaURI);
      } catch (URISyntaxException ex) {
        // TODO: throw an appropriate exception
        throw new RuntimeException(ex);
      } catch (InvalidJSONSchemaURIException ex) {
        // TODO: throw an appropriate exception
        throw new RuntimeException(ex);
      }
    } else {
      schemaVersion = JSONSchemaVersion.DEFAULT_VERSION;
      schemaURI = schemaVersion.URIs()[0];
    }
  }

  public JSONSchemaVersion version() {
    return schemaVersion;
  }

  public JSONValue jsonRoot() {
    return root;
  }

  /**
   * Read the given JSON Text (JSON Document) as JSON Schema.
   *
   * @param jsonText JSON Text
   * @return data model of JSON Schema
   */
  public static JSONRootSchema readAsSchema(JSONText jsonText)
      throws IOException, JSONParserException, InvalidJSONSchemaURIException {

    JSONValue rootValue = jsonText.parse();
    if (rootValue == null || rootValue.type() != JSONValueType.OBJECT) {
      // TODO: throw an appropriate exception
      throw new RuntimeException("the root JSON value must be an object for JSON Schema");

      // TODO: handle true and false as valid JSON Schema
      //       https://json-schema.org/draft/2019-09/json-schema-core.html#rfc.section.4.3.2
    }

    return new JSONRootSchema((JSONValueObject) rootValue);
  }
}
