package com.github.tnakamot.jscdg.definition.property;

import com.github.tnakamot.jscdg.definition.keyword.StringKeyword;
import com.github.tnakamot.jscdg.definition.keyword.IntegerKeyword;
import com.github.tnakamot.jscdg.definition.keyword.StringArrayKeyword;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JSONObjectProperty extends JSONProperty {
    public static final StringKeyword ID = new StringKeyword("$id");
    public static final IntegerKeyword MIN_PROPERTIES
            = new IntegerKeyword("minProperties");
    public static final IntegerKeyword MAX_PROPERTIES
            = new IntegerKeyword("maxProperties");
    public static final StringArrayKeyword REQUIRED
            = new StringArrayKeyword("required");

    // TODO: support additionalProperties
    // TODO: support propertyNames
    // TODO: support dependencies
    // TODO: support patternProperties

    private final Map<String, JSONProperty> properties;

    public JSONObjectProperty(String name, JSONObject obj)
            throws UnsupportedPropertyTypeException {
        super(name, obj);
        registerKeyword(ID);
        registerKeyword(MIN_PROPERTIES);
        registerKeyword(MAX_PROPERTIES);
        registerKeyword(REQUIRED);

        readAttributes(obj);

        properties = new HashMap<>();
        readProperties(obj);
    }

    public JSONProperty getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    private void readProperties(JSONObject obj) {
        Object propsObj = obj.get("properties");
        if (!(propsObj instanceof JSONObject)) {
            // Ignore if "properties" are not defined as JSONObject.
        } else {
            JSONObject props = (JSONObject) propsObj;
            Set propNames = props.keySet();
            propNames.forEach( (propNameObj) -> {
                Object propObj = props.get(propNameObj);

                if (propNameObj instanceof String &&
                        propObj instanceof JSONObject) {
                    // Ignore the property if the name is not defined as
                    // string or if the property is not defined as JSONObject.

                    String propName = (String) propNameObj;
                    JSONProperty jprop = JSONProperty.convert(propName, (JSONObject) propObj);
                    if (jprop != null) {
                        properties.put(propName, jprop);
                    }
                }
            });
        }
    }
}
