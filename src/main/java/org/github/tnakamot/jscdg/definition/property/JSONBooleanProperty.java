package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.BooleanArrayKeyword;
import org.github.tnakamot.jscdg.definition.keyword.BooleanKeyword;
import org.json.simple.JSONObject;

public class JSONBooleanProperty extends JSONProperty {
    public static final BooleanKeyword DEFAULT           = new BooleanKeyword("default");
    public static final BooleanKeyword CONST             = new BooleanKeyword("const");
    public static final BooleanArrayKeyword EXAMPLES
            = new BooleanArrayKeyword("examples");

    public JSONBooleanProperty(String name, JSONObject obj) {
        super(name, obj);
        registerKeyword(DEFAULT);
        registerKeyword(CONST);
        registerKeyword(EXAMPLES);

        readAttributes(obj);
    }
}
