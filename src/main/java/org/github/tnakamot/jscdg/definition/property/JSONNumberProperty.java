package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.NumberArrayKeyword;
import org.github.tnakamot.jscdg.definition.keyword.NumberKeyword;
import org.json.simple.JSONObject;

public class JSONNumberProperty extends JSONProperty {
    public static final NumberKeyword MULTIPLE_OF       = new NumberKeyword("multipleOf");
    public static final NumberKeyword MINIMUM           = new NumberKeyword("minimum");
    public static final NumberKeyword EXCLUSIVE_MINIMUM = new NumberKeyword("exclusiveMinimum");
    public static final NumberKeyword MAXIMUM           = new NumberKeyword("maximum");
    public static final NumberKeyword EXCLUSIVE_MAXIMUM = new NumberKeyword("exclusiveMaximum");
    public static final NumberKeyword DEFAULT           = new NumberKeyword("default");
    public static final NumberKeyword CONST             = new NumberKeyword("const");

    public static final NumberArrayKeyword EXAMPLES
            = new NumberArrayKeyword("examples");
    public static final NumberArrayKeyword ENUMS
            = new NumberArrayKeyword("enums");

    public JSONNumberProperty(String name, JSONObject obj) {
        super(name, obj);
        registerKeyword(MULTIPLE_OF);
        registerKeyword(MINIMUM);
        registerKeyword(EXCLUSIVE_MINIMUM);
        registerKeyword(MAXIMUM);
        registerKeyword(EXCLUSIVE_MAXIMUM);
        registerKeyword(DEFAULT);
        registerKeyword(CONST);
        registerKeyword(EXAMPLES);
        registerKeyword(ENUMS);

        readAttributes(obj);
    }
}
