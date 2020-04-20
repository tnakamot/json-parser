package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.BooleanKeyword;
import org.github.tnakamot.jscdg.definition.keyword.IntegerKeyword;
import org.github.tnakamot.jscdg.definition.keyword.StringArrayKeyword;
import org.github.tnakamot.jscdg.definition.keyword.StringKeyword;
import org.json.simple.JSONObject;

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

    public JSONObjectProperty(String name, JSONObject obj)
            throws UnsupportedPropertyTypeException {
        super(name, obj);
        registerKeyword(ID);
        registerKeyword(MIN_PROPERTIES);
        registerKeyword(MAX_PROPERTIES);
        registerKeyword(REQUIRED);

        readAttributes(obj);
    }
}
