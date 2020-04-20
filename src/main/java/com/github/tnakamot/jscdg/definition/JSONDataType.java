package com.github.tnakamot.jscdg.definition;

import com.github.tnakamot.jscdg.definition.value.*;

public class JSONDataType {
    public static final JSONDataType STRING  = new JSONDataType("string", JSONStringValue.class);
    public static final JSONDataType INTEGER = new JSONDataType("integer", JSONIntegerValue.class);
    public static final JSONDataType NUMBER  = new JSONDataType("number", JSONNumberValue.class);
    public static final JSONDataType BOOLEAN =  new JSONDataType("boolean", JSONBooleanValue.class);

    public static final JSONDataType STRING_ARRAY  = new JSONDataType("string_array", JSONStringArrayValue.class);
    public static final JSONDataType INTEGER_ARRAY = new JSONDataType("integer_array", JSONIntegerArrayValue.class);
    public static final JSONDataType NUMBER_ARRAY  = new JSONDataType("number_array", JSONNumberArrayValue.class);
    public static final JSONDataType BOOLEAN_ARRAY =  new JSONDataType("boolean_array", JSONBooleanArrayValue.class);


    private final String typeName;
    private final Class valueClass;

    private JSONDataType(String typeName, Class valueClass) {
        this.typeName   = typeName;
        this.valueClass = valueClass;

        if (!JSONValue.class.isAssignableFrom(valueClass)) {
            throw new IllegalArgumentException("valueClass must be a child class of JSONValue.");
        }
    }

    public String getTypeName() { return typeName; }
    public Class  getValueClass() { return valueClass; }

    @Override
    public String toString() { return typeName; }
}
