package org.github.tnakamot.jscdg.definition.property;

public enum JSONPropertyType {
    STRING("string", JSONStringProperty.class),
    INTEGER("integer", JSONIntegerProperty.class),
    NUMBER("number", JSONNumberProperty.class),
    BOOLEAN("boolean", JSONBooleanProperty.class),
    OBJECT("object", JSONObjectProperty.class);

    // TODO: support "array"
    // TODO: support "null"

    private final String name;
    private final Class propertyClass;

    JSONPropertyType(String name, Class propertyClass) {
        this.name = name;
        this.propertyClass = propertyClass;
    }

    public String getName() {
        return name;
    }

    public Class getPropertyClass() {
        return propertyClass;
    }

    static JSONPropertyType fromName(String name) {
        for (JSONPropertyType type: values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Type name '" + name + "' is not supported.");
    }

    @Override
    public String toString(){ return name; }
}
