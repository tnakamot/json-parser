package org.github.tnakamot.jscdg.definition;

import org.github.tnakamot.jscdg.definition.value.*;

public class JSONPrimitiveType {
    public static final JSONPrimitiveType STRING  = new JSONPrimitiveType("string", JSONStringValue.class);
    public static final JSONPrimitiveType INTEGER = new JSONPrimitiveType("integer", JSONIntegerValue.class);
    public static final JSONPrimitiveType NUMBER  = new JSONPrimitiveType("number", JSONNumberValue.class);
    public static final JSONPrimitiveType BOOLEAN =  new JSONPrimitiveType("boolean", JSONBooleanValue.class);

    private final String typeName;
    private final Class valueClass;

    private JSONPrimitiveType(String typeName, Class valueClass) {
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
