package com.github.tnakamot.jscdg.definition.property;

import com.github.tnakamot.jscdg.definition.keyword.IntegerArrayKeyword;
import com.github.tnakamot.jscdg.definition.keyword.IntegerKeyword;
import org.json.simple.JSONObject;

public class JSONIntegerProperty extends JSONProperty {
    public static final IntegerKeyword MULTIPLE_OF       = new IntegerKeyword("multipleOf");
    public static final IntegerKeyword MINIMUM           = new IntegerKeyword("minimum");
    public static final IntegerKeyword EXCLUSIVE_MINIMUM = new IntegerKeyword("exclusiveMinimum");
    public static final IntegerKeyword MAXIMUM           = new IntegerKeyword("maximum");
    public static final IntegerKeyword EXCLUSIVE_MAXIMUM = new IntegerKeyword("exclusiveMaximum");
    public static final IntegerKeyword DEFAULT           = new IntegerKeyword("default");
    public static final IntegerKeyword CONST             = new IntegerKeyword("const");

    public static final IntegerArrayKeyword EXAMPLES
            = new IntegerArrayKeyword("examples");
    public static final IntegerArrayKeyword ENUMS
            = new IntegerArrayKeyword("enums");

    public JSONIntegerProperty(String name, JSONObject obj)
            throws UnsupportedPropertyTypeException{
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
