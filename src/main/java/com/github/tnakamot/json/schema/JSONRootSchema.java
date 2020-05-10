package com.github.tnakamot.json.schema;

import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.parser.JSONParserException;
import com.github.tnakamot.json.value.JSONValue;
import com.github.tnakamot.json.value.JSONValueObject;
import com.github.tnakamot.json.value.JSONValueType;
import java.io.IOException;
import org.dmfs.rfc3986.uris.Text;

public class JSONRootSchema extends JSONSchema {
  private final JSONSchemaVersion schemaVersion;
  private final String schemaURI;
  private final JSONValueObject root;

  private JSONRootSchema(JSONValueObject root) throws InvalidJSONSchemaURIException {
    this.root = root;

    if (root.containsKey("$schema")) {
      this.schemaURI = root.getString("$schema");
      this.schemaVersion = JSONSchemaVersion.fromURI(this.schemaURI);
    } else {
      this.schemaVersion = JSONSchemaVersion.DEFAULT_VERSION;
      this.schemaURI = new Text(this.schemaVersion.URIs()[0]).toString();
    }
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
    if (rootValue.type() != JSONValueType.OBJECT) {
      // TODO: throw an appropriate exception
      throw new RuntimeException("the root JSON value must be an object for JSON Schema");

      // TODO: handle true and false as valid JSON Schema
      //       https://json-schema.org/draft/2019-09/json-schema-core.html#rfc.section.4.3.2
    }

    JSONRootSchema rootSchema = new JSONRootSchema((JSONValueObject) rootValue);

    return rootSchema;
  }
}
