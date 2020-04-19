package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.IntegerKeyword;
import org.github.tnakamot.jscdg.definition.keyword.StringArrayKeyword;
import org.github.tnakamot.jscdg.definition.keyword.StringKeyword;
import org.json.simple.JSONObject;

public class JSONStringProperty extends JSONProperty {
    public static final IntegerKeyword MIN_LENGTH = new IntegerKeyword("minLength");
    public static final IntegerKeyword MAX_LENGTH = new IntegerKeyword("maxLength");
    public static final StringKeyword  PATTERN    = new StringKeyword("pattern");
    public static final StringKeyword  FORMAT     = new StringKeyword("format");

    public static final StringKeyword DEFAULT = new StringKeyword("default");
    public static final StringKeyword CONST   = new StringKeyword("const");

    public static final StringArrayKeyword EXAMPLES
            = new StringArrayKeyword("examples");
    public static final StringArrayKeyword ENUMS
            = new StringArrayKeyword("enums");

    public JSONStringProperty(String name, JSONObject obj) {
        super(name, obj);
        registerKeyword(MIN_LENGTH);
        registerKeyword(MAX_LENGTH);
        registerKeyword(PATTERN);
        registerKeyword(FORMAT);
        registerKeyword(DEFAULT);
        registerKeyword(CONST);
        registerKeyword(EXAMPLES);
        registerKeyword(ENUMS);

        readAttributes(obj);
    }
}
